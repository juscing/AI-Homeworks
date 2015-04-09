from negotiator_base import BaseNegotiator


class JustinBot(BaseNegotiator):
    def __init__(self):
        super().__init__()
        self.enemy_utility_history = []
        self.our_utility_history = []
        self.enemy_offer_history = []
        self.our_offer_history = []
        self.goingFirst = None
        self.turnsTaken = 0
        self.ourScoreHistory = []
        self.enemyScoreHistory = []
        self.resultHistory = []

    # initialize(self : BaseNegotiator, preferences : list(String), iter_limit : Int)
        # Performs per-round initialization - takes in a list of items, ordered by the item's
        # preferability for this negotiator
        # You can do other work here, but still need to store the preferences
    def initialize(self, preferences, iter_limit):
        super().initialize(preferences,iter_limit)
        print(self.preferences)
        self.goingFirst = None
        self.turnsTaken = 0
        self.enemy_utility_history.clear()
        self.our_utility_history.clear()

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
        """
        print(self.turnsTaken)
        if offer:
            self.enemy_offer_history.append(offer)
            if self.goingFirst is None:
                self.goingFirst = False
                print("I'm not going first!")
        else:
            if self.goingFirst is None:
                self.goingFirst = True
                print("I'm going first!")

        # Let's always begin by making our ideal offer
        if self.turnsTaken == 0:
            self.offer = self.preferences[:]

        #Lets assume we are not going to accept the offer first
        acceptOffer = False

        # If the offer for them is better for them than their last offer, reject it

        # Only make concessions if the other guy makes concessions

        # This is the last offer!! Person going first has to choose whether to accept or not
        if not self.goingFirst and self.turnsTaken == self.iter_limit - 1:
            print("make last offer!")
            pass

        # If we go first, and this is the last turn, we can decide whether or not to accept the final offer
        if self.goingFirst and self.turnsTaken == self.iter_limit:
            print("decision on last offer!")
            pass

        if acceptOffer:
            self.offer = offer[:]

        # store our offer history
        self.our_offer_history.append(self.offer)
        # turns taken increases
        self.turnsTaken += 1
        # return the offer
        return self.offer

    def utility(self):
        utility = super().utility()
        self.our_utility_history.append(utility)
        return utility

    # receive_utility(self : BaseNegotiator, utility : Float)
        # Store the utility the other negotiator received from their last offer
    def receive_utility(self, utility):
        self.enemy_utility_history.append(utility)

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