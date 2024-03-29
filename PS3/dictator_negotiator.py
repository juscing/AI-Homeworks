#jci5kb Justin Ingram
#mgb5db Megan Bishop

from random import shuffle
from negotiator_base import BaseNegotiator

class DictatorBot(BaseNegotiator):
    iteration_limit = 500

    def __init__(self):
        super().__init__()
        # History of offers
        self.our_offer_history = []
        self.enemy_offer_history = []
        self.offer.clear()

        # The enemy's best offer so far
        self.enemy_max_offer = []

        # Utility Histories
        # How much we will get from our own offers
        self.our_offer_utility_history = []
        # How much the enemy would get from our own offer, scaled to be directly 
        #comparable
        self.our_offer_enemy_utility_history = []
        # Incoming, randomly scaled utility of enemy's offers
        self.enemy_offer_rawutility_history = []
        # What we expect the enemy to get from their offer, scaled to be comparable 
        #to our utilities
        self.enemy_utility_from_enemy_offer_history = []
        # What we expect to get from the enemy's offer
        self.our_utility_from_enemy_offer_history = []

        # The actual results (WIN, NUM TURNS)
        self.resultHistory = []

        # Score history
        self.ourScoreHistory = []
        self.enemyScoreHistory = []

        # Numerical items
        self.turnsTaken = 0
        self.max_utility = 0
        self.enemy_max_utility = 0
        self.our_preferences_on_enemy_scale = 0

        self.goingFirst = None
        self.acceptOffer = False
        self.currentLow = 0
        self.lastLow = 0

    # initialize(self : BaseNegotiator, preferences : list(String), iter_limit : Int)
        # Performs per-round initialization - takes in a list of items, ordered by 
        # the item's
        # preferability for this negotiator
        # You can do other work here, but still need to store the preferences

    def initialize(self, preferences, iter_limit):
        super().initialize(preferences, iter_limit)
        #print("Our preferences " + str(self.preferences))
        # Reset all our fields that do not carry over from the past run
        self.goingFirst = None
        self.turnsTaken = 0
        self.acceptOffer = False
        # Offer histories
        self.enemy_offer_history.clear()
        self.our_offer_history.clear()

        # Utility histories
        self.our_offer_utility_history.clear()
        self.our_offer_enemy_utility_history.clear()
        self.enemy_offer_rawutility_history.clear()
        self.enemy_utility_from_enemy_offer_history.clear()
        self.our_utility_from_enemy_offer_history.clear()

        self.enemy_max_utility = float("-inf")
        self.our_preferences_on_enemy_scale = 0
        self.enemy_max_offer.clear()

        self.offer.clear()

        # Set our max utility to be the value of the preference utility
        self.max_utility = self.calculate_offer_utility(preferences)
        self.currentLow = self.max_utility
        self.lastLow = self.max_utility * 0.75

    # make_offer(self : BaseNegotiator, offer : list(String)) --> list(String)
    # Given the opposing negotiator's last offer (represented as an ordered list),
    # return a new offer. If you wish to accept an offer & end negotiations,
    #return the same offer
    # Note: Store a copy of whatever offer you make in self.offer at the end of
    #this method.

    def make_offer(self, offer):


        ### All the calculations up here
        #print("Turn #" + str(self.turnsTaken))
        if offer:
        
            #print("Enemy offer: " + str(offer))
            self.enemy_offer_history.append(offer)

            #print("Reported utility for this offer " + str(self.enemy_offer_rawutility_history[-1]))

            if self.enemy_offer_rawutility_history[-1] > self.enemy_max_utility:
                #print("This is the new best guess for their preferences!")
                self.enemy_max_utility = self.enemy_offer_rawutility_history[-1]

                #print("New max enemy utility is " + str(self.enemy_max_utility))

                # recalculate...
                self.enemy_max_offer = offer[:]

                # Our preferences on the enemy's estimated scale
                self.our_preferences_on_enemy_scale = self.calculate_our_offer_on_enemy_scale(self.preferences)
                #print("Our preferences on the enemy's estimated utility: " + str(self.our_preferences_on_enemy_scale))

                # Previous offers now based on this new preferred ordering
                #print("Recalculating previous utility estimates")
                self.enemy_utility_from_enemy_offer_history.clear()
                for ordering in self.enemy_utility_from_enemy_offer_history:
                    self.enemy_utility_from_enemy_offer_history.append(self.calculate_our_offer_on_enemy_scale(offer))
                #print(self.enemy_utility_from_enemy_offer_history)

            # Now that we have reset the best estimate
            # Get our utility from this offer
            self.our_utility_from_enemy_offer_history.append(self.calculate_offer_utility(offer))
            #print("Our utility from enemy's offer: " + str(self.our_utility_from_enemy_offer_history[-1]))

            # Estimate enemy's utility from the offer
            self.enemy_utility_from_enemy_offer_history.append(self.calculate_our_offer_on_enemy_scale(offer))
            #print("Estimated utility enemy receives from their offer: " + str(
            #    self.enemy_utility_from_enemy_offer_history[-1]))
            
            if self.goingFirst is None:
                self.goingFirst = False
                #print("I'm not going first!")
        else:
            if self.goingFirst is None:
                self.goingFirst = True
                #print("I'm going first!")


        ### Decision to accept reject in here
        if(offer):
            self.accept_offer(offer)

        if self.acceptOffer:
            self.offer = offer[:]


        ### Making Offers ###
        # Only make an offer if we are not accepting
        else:
            # Let's always begin by making our ideal offer
            if self.turnsTaken == 0:
                self.offer = self.preferences[:]
            else:
                self.offer = self.generate_offer()

            # This is the last offer!! Person going first has to choose whether to 
            # accept or not
            #if not self.goingFirst and self.turnsTaken == self.iter_limit - 1:
            #    print("make last offer!")

            ####### Storing the history of the offer we have decided to make #######

            # store our offer history
            self.our_offer_history.append(self.offer)

            # store the utility of the offer we are making
            self.our_offer_utility_history.append(self.utility())
            #print("Offer utility " + str(self.our_offer_utility_history[-1]))

        # turns taken increases
        self.turnsTaken += 1

        # return the offer
        return self.offer


    def generate_offer(self):
        # Will only go down to 75% of total and will only accept if it comes out on top
        # On subsequent runs if there was an agreement on the previous, it will increase
        #    its bottom threshold by 5%

        #what is 5% of total utility?
        fivePercent = (5 * self.max_utility) / 100
        # copy the preferences
        ordering = self.preferences[:]
        if self.currentLow > self.lastLow:
            self.currentLow = self.currentLow - fivePercent
            
        i = 0
        while i < DictatorBot.iteration_limit:
            # lets get a new ordering
            shuffle(ordering)
            #calculate its utility
            utility = self.calculate_offer_utility(ordering)
            # is it above the threshold?
            if utility >= self.currentLow:
                return ordering
            i += 1

        # we failed to generate one in the number of iterations specified
        #just put forth the last offer
        return self.our_offer_history[-1]

    def calculate_offer_utility(self, offer):
        backup = self.offer[:]
        self.offer = offer
        utility = self.utility()
        self.offer = backup[:]
        return utility


    def calculate_our_offer_on_enemy_scale(self, offer):
        backuppref = self.preferences[:]
        self.preferences = self.enemy_max_offer[:]
        backup = self.offer[:]
        self.offer = offer
        utility = self.utility()
        self.offer = backup[:]
        self.preferences = backuppref[:]
        return utility

    def convert_enemy_scaled_to_utility(self, ordering):
        pass

    # receive_utility(self : BaseNegotiator, utility : Float)
    # Store the utility the other negotiator received from their last offer
    def receive_utility(self, utility):
        self.enemy_offer_rawutility_history.append(utility)

    # receive_results(self : BaseNegotiator, results : (Boolean, Float, Float, Int))
    # Store the results of the last series of negotiation (points won, success, etc.)
    def receive_results(self, results):
        # Always from the same opponent
        self.resultHistory.append((results[0], results[3]))
        if self.goingFirst:
            self.ourScoreHistory.append(results[1])
            self.enemyScoreHistory.append(results[2])
        else:
            self.ourScoreHistory.append(results[2])
            self.enemyScoreHistory.append(results[1])

        if self.lastLow < self.currentLow:
            self.lastLow = self.currentLow
        #print(self.our_total_score())
        #print(self.enemy_total_score())

    def our_total_score(self):
        return sum(self.ourScoreHistory)

    def enemy_total_score(self):
        return sum(self.enemyScoreHistory)

    def accept_offer(self, offer):

        onTop = self.our_utility_from_enemy_offer_history[-1] > \
                self.enemy_utility_from_enemy_offer_history[-1]

        #Last Turn! Final Offer!  Either going to accept of reject.
        if self.goingFirst and self.turnsTaken == self.iter_limit:
            #print("decision on last offer!")
            #if we come out on top, accept
            if onTop:
                self.acceptOffer = True

        #All other turns: must be within 5% of what was just offered and we are on top
        else:
            #within 5%
            p = (self.currentLow / self.max_utility) * 100
            q = (self.our_utility_from_enemy_offer_history[-1] /
                self.max_utility) * 100
            if abs(p-q) <= 5:
                fivePercent = True
            else:
                fivePercent = False

            if onTop and fivePercent:
                self.acceptOffer = True


