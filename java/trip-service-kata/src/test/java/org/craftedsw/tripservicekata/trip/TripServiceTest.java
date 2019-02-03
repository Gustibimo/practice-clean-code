package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;


public class TripServiceTest {

    private static final User GUEST = null ;
    private static final User UNUSED_USER = null ;
    private static final Trip TO_WAGENINGEN = new Trip();
    private static final User ANOTHER_USER =  new User();
    private static final User REGISTERED_USER = new User();
    private static final Trip TO_BOGOR = new Trip();
    private User loggedInUser;
    private TripService tripService;

    @Before
    public void setUp() throws Exception {
        tripService = new TestableTripService();
        loggedInUser = REGISTERED_USER;

    }

    @Test (expected = UserNotLoggedInException.class) public void
    shouldThrowExceptionWhenUserNotLoggedIn() {

        loggedInUser = GUEST;


        tripService.getTripsByUser(UNUSED_USER);
    }
    @Test
    public void
    shouldNotReturnAnyTripIfUserNotFriends() {
        User friend = new User();
        friend.addFriend(ANOTHER_USER);
        friend.addTrip(TO_WAGENINGEN);
        List<Trip> friendTrips = tripService.getTripsByUser(friend);

        Assert.assertThat(friendTrips.size(), is(0));

    }


    @Test
    public void shouldReturnFriendTripsWhenUserAreFriend() {
        User friend = new User();
        friend.addFriend(ANOTHER_USER);
        friend.addFriend(loggedInUser);
        friend.addTrip(TO_WAGENINGEN);
        friend.addTrip(TO_BOGOR);
        List<Trip> friendTrips = tripService.getTripsByUser(friend);

        Assert.assertThat(friendTrips.size(), is(2));

    }

    private class TestableTripService extends TripService{

        @Override
        protected User getLoggedInUser() {
            return loggedInUser;
        }

        @Override
        protected List<Trip> tripsBy(User user) {
            return user.trips();
        }
    }
}
