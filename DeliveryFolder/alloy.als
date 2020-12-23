open util/integer

sig Time{
	time: one Int
}{
	time>=0 
}

sig Position{
	latitude: one Int,
	longitude: one Int,
}

sig DataTime{
	day: one Int,
	month: one Int, 
	time: one Int
}{
	day > 0
	month >0
	time >0
}

sig Department{
	name: one Int,
	maximum_capability: one Int
}{
	maximum_capability>0
}

sig Store{
	position: one Position,
	name: one Int,
	departments: set Department,
	actual_data: one DataTime,
	time_start: one Time,
	time_closure: one Time,
	average_time_of_visit: one Time,
	tolerance_of_delay: one Time, 
}

//It doesn't contain any other attributes (differently by the UML) because only these underwritten are useful for testing all requirements (except showing data)
sig Customer_Profile{
	average_visit_time: one Time,
	delta_delay: one Int,
	preferred_store: one Store
}{
	delta_delay >=0

}

abstract sig User{
	username: one Int,
	password: one Int
}

abstract sig Customer_status{}
sig InTheQueue extends Customer_status{}
sig Entered extends Customer_status{}
sig Inactive extends Customer_status{}

sig Customer extends User{
	position: one Position,
	profile: one Customer_Profile,
	cus_status: one Customer_status
}

sig Handout_Clerk extends User{
	associated_store: one Store
}

sig Scan_Clerk extends User{
	associated_store: one Store
}

sig Itinerary{
	store_chosen: one Store, 
	departments_list: set Department,
	duration_estimated: one Int
}{
	duration_estimated >=0
}

abstract sig Reservation_status{}
sig ToBeEvaluated extends Reservation_status{}
sig Pending extends Reservation_status{}
sig Accepted extends Reservation_status{}
sig Rejected extends Reservation_status{}
sig Removed extends Reservation_status{}

abstract sig Reservation{
	customer: one Customer,
	data_reservation: one DataTime,
	code: one Int,
	itinerary: one Itinerary,
	res_status: one Reservation_status,
	time_to_arrive: one Time //time for reach the store
}

sig LineUp_Request extends Reservation{}

sig Booking_Request extends Reservation{
	data_booking: one DataTime
}


sig Monitor{
	reservation_turn: one Int
}

sig Reservation_Manager{
	lineUp_list: set LineUp_Request,
	booking_list: set Booking_Request,
	res_inside: set Reservation,
	store: one Store,
	monitor: one Monitor,
	maximum_waiting_time: one Time
}

abstract sig Suggestion{
	available_stores_list: set Store,
	available_times_list: set Int,
}

sig Suggestion_LineUp extends Suggestion{
	reservation_rejected: one LineUp_Request
}

sig Suggestion_Booking extends Suggestion{
	reservation_rejected: one Booking_Request
}

fact timeClosureAfterStart{
	all s:Store | s.time_closure.time > s.time_start.time
}


//constraints on time

--Every store has a data 
fact allStoreHasADataTime{
	all s: Store | one d: DataTime | s.actual_data = d
}

--Every store has the same data (supposing that the stores are in the same country with no time lag)
fact allStoreHasTheSameData{
	all s1,s2: Store | s1.actual_data = s2.actual_data
}

--Every line-up request could be done the same day in which the store is, it can't be done neither the previous day
fact allLineUpDoneSameDay{
	all lr: LineUp_Request | all s: Store | lr.data_reservation.month = s.actual_data.month and lr.data_reservation.day =s.actual_data.day
									and lr.data_reservation.time <= s.actual_data.time
}

--every CustomerProfile has a Customer that addresses to it
fact noFloatingProfiles{
	all cp: Customer_Profile | one c:Customer | c.profile=cp
}

--Every booking request must be done, obviously, in the past, even days before.
fact allBookingDoneInThePast{
	all b: Booking_Request | all s:Store | (b.data_reservation.month < s.actual_data.month and b.data_reservation.day - s.actual_data.day >= 25)
								 or (b.data_reservation.month = s.actual_data.month and b.data_reservation.day < s.actual_data.day)
								 or (b.data_reservation.month = s.actual_data.month and b.data_reservation.day = s.actual_data.day and
									b.data_reservation.time < s.actual_data.time)
}


--Every booking request is for the present or the future days
fact allBookingForTheFuture{
	all b: Booking_Request | no s:Store | b.data_booking.month < s.actual_data.month 
									or (b.data_booking.month = s.actual_data.month and b.data_booking.day < s.actual_data.day)
								 	or (b.data_booking.month = s.actual_data.month and b.data_booking.day = s.actual_data.day and
									b.data_booking.time < s.actual_data.time)
}

//constraints on Department

--Departments in the same store can't have the same name
fact noSameDepartmentName{
	all disj d1,d2: Department, s:Store | (d1 in s.departments and d2 in s.departments) implies d1.name!=d2.name
}

fact departmentIsOnlyInOneStore{
	all disj s1, s2: Store | no d: Department | d in s1.departments and d in s2.departments
}

//constraints on Store

-- Every store has a position
fact everyStoreHasAPosition{
	all s: Store | one p: Position | s.position = p
}


--Every store has a different position or name
fact everyStoreHasADifferentPositionOrName{
	all disj s1,s2: Store | s1.position!=s2.position or s1.name!=s2.name
}


--Every store has at least one department
fact everyStoreHasAtLeastOneDepartment{
	all s:Store | #s.departments>0
}



//constraint on User

--Two users can't have the same username
fact noSameUsername{
	all disj u1, u2: User | u1.username!=u2.username
}

//constraint on Customer

--No Customer Profile is shared
fact noSameCustomerProfile{
	all disj c1,c2: Customer | no cp: Customer_Profile | c1.profile = cp and c2.profile = cp
}

//no constraint on Handout_Clerk
//no constraint on Scan_Clerk

//constraint on Itinerary
fact departmentsBelongToAStore{
	all i: Itinerary | i.departments_list in i.store_chosen.departments
}

//constraints on Reservation, LineUp_Request and Booking_Request

fact noObiquity1{
	no disj l1,l2: LineUp_Request | l1.res_status = Accepted and l2.res_status = Accepted and l1.customer = l2.customer
}

fact noObiquity2{
	no disj b1,b2: Booking_Request | b1.res_status = Accepted and b2.res_status = Accepted and b1.customer = b2.customer
}
//no constraint on Monitor

//constraints on Reservation_Manager

--If a reservation is in reservation manager lists, it must have the same store associated to
fact allReservationInManagerIfSameStore1{
	all rm: Reservation_Manager | all l: LineUp_Request | l in rm.lineUp_list implies l.itinerary.store_chosen = rm.store 
}

--If a reservation is in reservation manager lists, it must have the same store associated to
fact allReservationInManagerIfSameStore2{
	all rm: Reservation_Manager | all b: Booking_Request | b in rm.booking_list implies b.itinerary.store_chosen = rm.store 
}

fact allReservationInsideTheStoreChosen{
	all rm: Reservation_Manager | all r: Reservation | r in rm.res_inside implies r.itinerary.store_chosen = rm.store 
}

fact everyManagerHasDifferentMonitors{
	no disj r1,r2: Reservation_Manager | r1.monitor = r2.monitor
}

//TO BE CHECKED
fact noDoubleLineUp{
	all r: Reservation_Manager | no disj l1,l2: LineUp_Request | l1.customer = l2.customer and l1 in r.lineUp_list and l2 in r.lineUp_list
}


--Two reservation manager can't have the same store associated
fact noSameResManager{
	all disj r1,r2: Reservation_Manager | r1.store!=r2.store
}

--Every store is covered by a reservation manager
fact everyStoreHasAReservationManager{
	all s: Store | one r: Reservation_Manager | r.store = s
}

fact onlyInOneList1{
	all rm: Reservation_Manager, l: rm.lineUp_list | l not in rm.res_inside
}

fact onlyInOneList2{
	all rm: Reservation_Manager, b: rm.booking_list | b not in rm.res_inside
}

fact onlyInOneList3{
	no rm: Reservation_Manager, r: rm.res_inside | r in rm.booking_list or r in rm.lineUp_list
}

fact everyResCantBeInMultipleManager1{
	all lr: LineUp_Request | no disj r1,r2: Reservation_Manager | (lr in r1.res_inside or lr in r1.lineUp_list) and (lr in r2.res_inside or lr in r2.lineUp_list)
}

fact everyResCantBeInMultipleManager2{
	all b: Booking_Request | no disj r1,r2: Reservation_Manager | (b in r1.res_inside or b in r1.booking_list) and (b in r2.res_inside or b in r2.booking_list)
}


//constraints on Suggestion

--Every lineUp suggestion can't suggest the same store chosen before
fact storeSuggestedNotEqualToChosen1{
	all s:Suggestion_LineUp | not s.reservation_rejected.itinerary.store_chosen in s.available_stores_list
}

--Every booking suggestion can't suggest the same store chosen before
fact storeSuggestedNotEqualToChosen2{
	all s:Suggestion_Booking | not s.reservation_rejected.itinerary.store_chosen in s.available_stores_list
}

--Every booking suggestion can't suggest the same time chosen before
fact timeSuggestedNotEqualChosen{
	all s: Suggestion_Booking | not s.reservation_rejected.data_booking.time in s.available_times_list
}

--LineUp suggestions have no times 
fact noneTimesInLineUpSuggestion{
	all s: Suggestion_LineUp | s.available_times_list = none
}

--No same suggesttion
fact noSameSuggestion{
	no disj s1,s2: Suggestion_LineUp | s1.reservation_rejected = s2.reservation_rejected
}
--No same suggestion
fact noSameSuggestion2{
	no disj s1,s2: Suggestion_Booking | s1.reservation_rejected = s2.reservation_rejected
}


//constraints on Reservation Status

--Define every status for LineUp Request
fact defineLineUpStatus{
	all lr: LineUp_Request | ((lr.res_status = ToBeEvaluated or lr.res_status = Rejected or lr.res_status = Removed) iff
						( no r: Reservation_Manager |  lr in r.res_inside or lr in r.lineUp_list)) and
						(lr.res_status = Pending iff (one r: Reservation_Manager | r.store = lr.itinerary.store_chosen and not lr in r.res_inside
						 and lr in r.lineUp_list)) and
						(lr.res_status = Accepted iff (one r: Reservation_Manager | r.store = lr.itinerary.store_chosen and lr in r.res_inside and
							 not lr in r.lineUp_list))
}

--Define every status for Booking Request
fact defineBookingStatus{
	all b: Booking_Request | ((b.res_status = ToBeEvaluated or b.res_status = Rejected or b.res_status = Removed) iff
						( no r: Reservation_Manager |  b in r.res_inside or b in r.booking_list)) and
						(b.res_status = Pending iff (one r: Reservation_Manager | r.store = b.itinerary.store_chosen and not b in r.res_inside
						 and b in r.booking_list)) and
						(b.res_status = Accepted iff (one r: Reservation_Manager | r.store = b.itinerary.store_chosen and b in r.res_inside and 
						not b in r.booking_list))
}

//constraint on customer status
--Define every customer status
fact defineCustomerStatus{
	all c: Customer | ((c.cus_status = InTheQueue iff (some r:Reservation | r.res_status = Pending and r.customer = c)) and 
					(c.cus_status = Entered iff (some r: Reservation | r.res_status = Accepted and r.customer = c))) and
					(c.cus_status = Inactive iff (no r: Reservation | r.customer = c and (r.res_status = Pending or r.res_status=Accepted)))
} 

//other constraints

--Only rejected reservations have a suggestion 
fact suggestionForDeterminedStatus1{
	all lr: LineUp_Request | one s: Suggestion_LineUp | lr.res_status = Rejected iff s.reservation_rejected = lr
}

--Only rejected reservations have a suggestion
fact suggestionForDeterminedStatus2{
	all b: Booking_Request | one s: Suggestion_Booking | b.res_status = Rejected iff s.reservation_rejected = b
}

fact noFloatingDepartments{
	all d:Department | one s:Store | d in s.departments
}

fact noBeforeActualSuggestion{
    all s: Suggestion_Booking, t: Time | one st:Store | s.reservation_rejected.itinerary.store_chosen = st and t.time > st.actual_data.time and
    t.time in s.available_times_list
}


pred show{
	#Store = 2
	#Reservation = 3
}


//The queue of line-up is not empty
pred notEmptyQueue[rm: Reservation_Manager, t_c: Int, t_estimated: Int]{
	#rm.lineUp_list> 0 or
	(some ti: DataTime | (ti in rm.booking_list.data_booking) and ti.time>=t_c and (ti.time < t_c +  t_estimated))
}


//Customer  insert time estimation (it's optional)
pred timeEstimationInserted[i: Itinerary]{
	some ti: DataTime.time | ti in i.duration_estimated
}

//Customer is a new users, so he/she his/hers average visit time and delta delay = 0
pred newUser[c: Customer]{
	c.profile.average_visit_time.time=0 and c.profile.delta_delay=0
}

//Customer isn't a new users, so he/she his/hers average visit time and delta delay >= 0
pred noNewUser[c: Customer]{
	c.profile.average_visit_time.time > 0 and c.profile.delta_delay>=0
}

//The queue is empty and the customer is a new user, so every data comes from store data and customer position
pred storeAvailability1[rm: Reservation_Manager, r: Reservation]{
	newUser[r.customer]
	(r in rm.booking_list or r in rm.lineUp_list)
	not timeEstimationInserted[r.itinerary]
	not notEmptyQueue[rm,rm.store.actual_data.time.add[r.time_to_arrive.time], rm.store.average_time_of_visit.time ]
	(rm.store.time_start.time<=	rm.store.actual_data.time.add[rm.store.average_time_of_visit.time].add[r.time_to_arrive.time] and 
	rm.store.actual_data.time.add[rm.store.average_time_of_visit.time].add[r.time_to_arrive.time] < rm.store.time_closure.time)
}

//Not a new user, empty queue, not inserted estimation
pred storeAvailability2[rm: Reservation_Manager, r: Reservation]{
	noNewUser[r.customer]
	(r in rm.booking_list or r in rm.lineUp_list)
	not timeEstimationInserted[r.itinerary]
	not notEmptyQueue[rm,rm.store.actual_data.time.add[r.time_to_arrive.time], rm.store.average_time_of_visit.time ]
	(rm.store.time_start.time<= rm.store.actual_data.time.add[ r.customer.profile.average_visit_time.time].add[r.time_to_arrive.time] and
    	rm.store.actual_data.time.add[ r.customer.profile.average_visit_time.time].add[r.time_to_arrive.time] < rm.store.time_closure.time)
}

//Time inserted, empty queue
pred storeAvailability3[rm: Reservation_Manager, r: Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
	not notEmptyQueue[rm,rm.store.actual_data.time.add[r.time_to_arrive.time], r.itinerary.duration_estimated]
	timeEstimationInserted[r.itinerary]
	(rm.store.time_start.time<=rm.store.actual_data.time.add[r.itinerary.duration_estimated].add[ r.time_to_arrive.time] and
	rm.store.actual_data.time.add[r.itinerary.duration_estimated].add[ r.time_to_arrive.time] < rm.store.time_closure.time)
}

pred arriveAfterEndQueue[rm: Reservation_Manager, r: Reservation] {
	rm.maximum_waiting_time.time <r.time_to_arrive.time
}

//New Customer will arrive after the end of the queue
pred storeAvailability4[rm: Reservation_Manager, r: Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
     notEmptyQueue[rm,rm.store.actual_data.time.add[r.time_to_arrive.time], rm.store.average_time_of_visit.time ]
	newUser[r.customer]
	not timeEstimationInserted[r.itinerary]
	arriveAfterEndQueue[rm, r]
	(rm.store.time_start.time<=	rm.store.actual_data.time.add[rm.store.average_time_of_visit.time].add[r.time_to_arrive.time]  and 
	rm.store.actual_data.time.add[rm.store.average_time_of_visit.time].add[r.time_to_arrive.time]  < rm.store.time_closure.time	)
}

//New Customer will arrive before the end of the queue
pred storeAvailability5[rm: Reservation_Manager,  r:Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
	notEmptyQueue[rm, rm.store.actual_data.time.add[rm.maximum_waiting_time.time], rm.store.average_time_of_visit.time ]
	newUser[r.customer]
	not timeEstimationInserted[r.itinerary]
	not arriveAfterEndQueue[rm, r]
	(rm.store.time_start.time <=	rm.store.actual_data.time.add[rm.store.average_time_of_visit.time].add[rm.maximum_waiting_time.time]  and
	rm.store.actual_data.time.add[rm.store.average_time_of_visit.time].add[rm.maximum_waiting_time.time]  < rm.store.time_closure.time)
}

//Customer will arrive after the end of the queue
pred storeAvailability6[rm: Reservation_Manager,  r:Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
	notEmptyQueue[rm, rm.store.actual_data.time.add[r.time_to_arrive.time], r.customer.profile.average_visit_time.time]
	not newUser[r.customer]
	not timeEstimationInserted[r.itinerary]
	arriveAfterEndQueue[rm, r]
	(rm.store.time_start.time <=	rm.store.actual_data.time.add[r.customer.profile.average_visit_time.time].add[r.time_to_arrive.time]  and
	rm.store.actual_data.time.add[r.customer.profile.average_visit_time.time].add[r.time_to_arrive.time]  < rm.store.time_closure.time)
}

//Customer will arrive before the end of the queue
pred storeAvailability7[rm: Reservation_Manager,  r:Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
	notEmptyQueue[rm,  rm.store.actual_data.time.add[rm.maximum_waiting_time.time], r.customer.profile.average_visit_time.time]
	not newUser[r.customer]
	not timeEstimationInserted[r.itinerary]
	not arriveAfterEndQueue[rm, r]
	(rm.store.time_start.time <= rm.store.actual_data.time.add[r.customer.profile.average_visit_time.time].add[rm.maximum_waiting_time.time]  and
	rm.store.actual_data.time.add[r.customer.profile.average_visit_time.time].add[rm.maximum_waiting_time.time]  < rm.store.time_closure.time)
}


//Customer will arrive after the end of the queue and estimate time (it doesn't matter if he/she is a new user)
pred storeAvailability8[rm: Reservation_Manager,  r:Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
	notEmptyQueue[rm, rm.store.actual_data.time + r.time_to_arrive.time, r.itinerary.duration_estimated]
	timeEstimationInserted[r.itinerary]
	arriveAfterEndQueue[rm, r]
	(rm.store.time_start.time <=	rm.store.actual_data.time.add[r.itinerary.duration_estimated].add[r.time_to_arrive.time]  and
	rm.store.actual_data.time.add[r.itinerary.duration_estimated].add[r.time_to_arrive.time]  < rm.store.time_closure.time)
}

//Customer will arrive before the end of the queue and estimate time (it doesn't matter if he/she is a new user)
pred storeAvailability9[rm :Reservation_Manager,  r:Reservation]{
	(r in rm.booking_list or r in rm.lineUp_list)
	notEmptyQueue[rm, rm.store.actual_data.time.add[rm.maximum_waiting_time.time], r.itinerary.duration_estimated]
	timeEstimationInserted[r.itinerary]
	not arriveAfterEndQueue[rm, r]
	(rm.store.time_start.time <= 	rm.store.actual_data.time.add[r.itinerary.duration_estimated].add[rm.maximum_waiting_time.time]  and
	rm.store.actual_data.time.add[r.itinerary.duration_estimated].add[rm.maximum_waiting_time.time]  < rm.store.time_closure.time)
}

//Combination of all possible cases
pred isStoreAvailable[s :Reservation_Manager, r:Reservation]{
	storeAvailability1[s,r] or storeAvailability2[s,r] or storeAvailability3[s,r] or storeAvailability4[s,r] or storeAvailability5[s,r] or storeAvailability6[s,r] or storeAvailability7[s,r]
 	or storeAvailability8[s,r] or storeAvailability9[s,r]
}


pred addRequestInTheQueue[l: LineUp_Request, rmAfter: Reservation_Manager, rmBefore: Reservation_Manager]{
	rmAfter.lineUp_list = rmBefore.lineUp_list + l
}

pred addLineUpInList[l: LineUp_Request, rm: Reservation_Manager]{
	l not in rm.lineUp_list implies (
	l.itinerary.store_chosen = rm.store and
	isStoreAvailable[rm, l] and
	addRequestInTheQueue[l, rm, rm])
}

pred removeRequestInTheQueue[l: LineUp_Request,  rmAfter: Reservation_Manager, rmBefore: Reservation_Manager]{
	rmAfter.lineUp_list  = rmBefore.lineUp_list  - l
}


pred removeLineUpInList[l: LineUp_Request, rm: Reservation_Manager]{
	l in rm.lineUp_list implies
	removeRequestInTheQueue[l, rm, rm]
}

assert addRemove{
	all rm: Reservation_Manager, lr: LineUp_Request |
						(addLineUpInList[lr,rm] implies removeLineUpInList[lr,rm]) implies lr not in rm.lineUp_list
}

pred noBookingInTheList[rm: Reservation_Manager]{
    #rm.booking_list = 0
} 

pred addBookingRequestInBookingSet[b: Booking_Request, rmAfter,rmBefore: Reservation_Manager]{
	rmAfter.booking_list = rmBefore.booking_list + b
}

pred addBooking[b: Booking_Request, rm: Reservation_Manager]{
    b not in rm.booking_list implies (
    (b.itinerary.store_chosen = rm.store and isStoreAvailable[rm,b]) and 
    addBookingRequestInBookingSet[b, rm, rm] )
}

pred removeBooking[b: Booking_Request,  rmAfter: Reservation_Manager, rmBefore: Reservation_Manager]{
	rmAfter.booking_list  = rmBefore.booking_list  - b
}

pred removeBook[b:Booking_Request, rm:Reservation_Manager]{
	b in rm.booking_list implies removeBooking[b,rm,rm]
}

fact noSameBooking{
	all rm:Reservation_Manager | no disj b1,b2:Booking_Request | b1 in rm.booking_list and b2 in rm.booking_list and b1.code = b2.code

}

assert addRemoveBooking{
	all rm: Reservation_Manager, b:Booking_Request |(addBooking[b,rm]  implies removeBook[b,rm]) implies b not in rm.booking_list
}

run removeBook
