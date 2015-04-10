from random import shuffle
from negotiator_base import BaseNegotiator


class JustinBot(BaseNegotiator):

    iteration_limit = 500

    def __init__(self):
        super().__init__()
        # History of offers
        self.our_offer_history = []
        self.enemy_offer_history = []

        # The enemy's best offer so far
        self.enemy_max_offer = []

        # Utility Histories
        # How much we will get from our own offers
        self.our_offer_utility_history = []
        # How much the enemy would get from our own offer, scaled to be directly comparable
        self.our_offer_enemy_utility_history = []
        # Incoming, randomly scaled utility of enemy's offers
        self.enemy_offer_rawutility_history = []
        # What we expect the enemy to get from their offer, scaled to be comparable to our utilities
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

    # initialize(self : BaseNegotiator, preferences : list(String), iter_limit : Int)
        # Performs per-round initialization - takes in a list of items, ordered by the item's
        # preferability for this negotiator
        # You can do other work here, but still need to store the preferences
    def initialize(self, preferences, iter_limit):
        super().initialize(preferences,iter_limit)
        print("Our preferences " + str(self.preferences))
        # Reset all our fields that do not carry over from the past run
        self.goingFirst = None
        self.turnsTaken = 0
        # Offer histories
        self.enemy_offer_history.clear()
        self.our_offer_history.clear()

        #Utility histories
        self.our_offer_utility_history.clear()
        self.our_offer_enemy_utility_history.clear()
        self.enemy_offer_rawutility_history.clear()
        self.enemy_utility_from_enemy_offer_history.clear()
        self.our_utility_from_enemy_offer_history.clear()

        self.enemy_max_utility = float("-inf")
        self.our_preferences_on_enemy_scale = 0
        self.enemy_max_offer.clear()

        # Set our max utility to be the value of the preference utility
        self.max_utility = self.calculate_offer_utility(preferences)

    # make_offer(self : BaseNegotiator, offer : list(String)) --> list(String)
        # Given the opposing negotiator's last offer (represented as an ordered list),
        # return a new offer. If you wish to accept an offer & end negotiations, return the same offer
        # Note: Store a copy of whatever offer you make in self.offer at the end of this method.
    def make_offer(self, offer):
        """
        Offer making strategies
        how much utility they give up before making a deal
        how many turns before they make a deal
        what is the lowest % of original utility before we give in?
        never make a concession unless they do
        assume first offer is preferences...
        give a random first offer?
        """

        """
        What we need
        proportion function
        an offer permuter function
        multiple ways for us to get same utility... we should try several?
        """

        ### All the calculations up here
        print("Turn #" + str(self.turnsTaken))
        if offer:
            print("Enemy offer: " + str(offer))
            self.enemy_offer_history.append(offer)

            print("Reported utility for this offer " + str(self.enemy_offer_rawutility_history[-1]))

            if self.enemy_offer_rawutility_history[-1] > self.enemy_max_utility:
                print("This is the new best guess for their preferences!")
                self.enemy_max_utility = self.enemy_offer_rawutility_history[-1]

                print("New max enemy utility is " + str(self.enemy_max_utility))

                # recalculate...
                self.enemy_max_offer = offer[:]

                # Our preferences on the enemy's estimated scale
                self.our_preferences_on_enemy_scale = self.calculate_our_offer_on_enemy_scale(self.preferences)
                print("Our preferences on the enemy's estimated utility: " + str(self.our_preferences_on_enemy_scale))

                # Previous offers now based on this new preferred ordering
                print("Recalculating previous utility estimates")
                self.enemy_utility_from_enemy_offer_history.clear()
                for ordering in self.enemy_utility_from_enemy_offer_history:
                    self.enemy_utility_from_enemy_offer_history.append(self.calculate_our_offer_on_enemy_scale(offer))
                print(self.enemy_utility_from_enemy_offer_history)

            # Now that we have reset the best estimate
            # Get our utility from this offer
            self.our_utility_from_enemy_offer_history.append(self.calculate_offer_utility(offer))
            print("Our utility from enemy's offer: " + str(self.our_utility_from_enemy_offer_history[-1]))

            # Estimate enemy's utility from the offer
            self.enemy_utility_from_enemy_offer_history.append(self.calculate_our_offer_on_enemy_scale(offer))
            print("Estimated utility enemy receives from their offer: " + str(self.enemy_utility_from_enemy_offer_history[-1]))

            if self.goingFirst is None:
                self.goingFirst = False
                print("I'm not going first!")
        else:
            if self.goingFirst is None:
                self.goingFirst = True
                print("I'm going first!")


        ### Decision to accept reject in here

        #Lets assume we are not going to accept the offer first
        acceptOffer = False

        # Because if there is no offer, there is no point to do this...
        if(offer):

            # If we go first, and this is the last turn, we can decide whether or not to accept the final offer
            if self.goingFirst and self.turnsTaken == self.iter_limit:
                print("decision on last offer!")

            # If we think that our utility is higher than theirs for this offer... accept
            if self.our_utility_from_enemy_offer_history[-1] > self.enemy_utility_from_enemy_offer_history[-1]:
                acceptOffer = True


        ### Making Offers ###

        # Only make an offer if we are not accepting
        if not acceptOffer:
            # Let's always begin by making our ideal offer
            if self.turnsTaken == 0:
                self.offer = self.preferences[:]
            else:
                self.offer = self.generate_offer(1-((1+self.turnsTaken)*0.05),1-(self.turnsTaken*0.05))

             # This is the last offer!! Person going first has to choose whether to accept or not
            if not self.goingFirst and self.turnsTaken == self.iter_limit - 1:
                print("make last offer!")


            ####### Storing the history of the offer we have decided to make #######

            # store our offer history
            self.our_offer_history.append(self.offer)

            # store the utility of the offer we are making
            self.our_offer_utility_history.append(self.utility())
            print("Offer utility " + str(self.our_offer_utility_history[-1]))

        else:
            ####### We decided to accept the offer #######
            self.offer = offer[:]




        # If the offer for them is better for them than their last offer, reject it

        # Only make concessions if the other guy makes concessions






        # turns taken increases
        self.turnsTaken += 1

        # return the offer
        return self.offer

    def generate_offer(self, lowpercent, highpercent):
        #higher bound is not flexible... lower bound is
        i = 0
        # copy the preferences
        ordering = self.preferences[:]
        while i < JustinBot.iteration_limit:
            # lets get a new ordering
            shuffle(ordering)
            #calculate its utility
            utility = self.calculate_offer_utility(ordering)
            # is it above the threshold?
            if lowpercent <= utility / self.max_utility <= highpercent:
                return ordering
            i+=1

        # we failed to generate one in the number of iterations specified...
        if lowpercent - 0.05 > 0:
            return self.generate_offer(lowpercent - 0.05, highpercent)
        elif highpercent + 0.05 < 1:
            return self.generate_offer(lowpercent, highpercent + 0.05)
        else:
            print("This should never happen, and you just screwed up")

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
        #Always from the same opponent
        self.resultHistory.append((results[0],results[3]))
        if self.goingFirst:
            self.ourScoreHistory.append(results[1])
            self.enemyScoreHistory.append(results[2])
        else:
            self.ourScoreHistory.append(results[2])
            self.enemyScoreHistory.append(results[1])

        print(self.our_total_score())
        print(self.enemy_total_score())

    def our_total_score(self):
        return sum(self.ourScoreHistory)

    def enemy_total_score(self):
        return sum(self.enemyScoreHistory)