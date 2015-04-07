from negotiator_base import BaseNegotiator


class JustinBot(BaseNegotiator):
    def __init__(self):
        super().__init__()
        self.enemy_utility_history = []
        self.my_utility_history = []
        self.enemy_offer_history = []
        self.my_offer_history = []
        self.results = []
        self.goingFirst = None

    # initialize(self : BaseNegotiator, preferences : list(String), iter_limit : Int)
        # Performs per-round initialization - takes in a list of items, ordered by the item's
        # preferability for this negotiator
        # You can do other work here, but still need to store the preferences
    def initialize(self, preferences, iter_limit):
        super().initialize(preferences,iter_limit)
        self.goingFirst = None

    # make_offer(self : BaseNegotiator, offer : list(String)) --> list(String)
        # Given the opposing negotiator's last offer (represented as an ordered list),
        # return a new offer. If you wish to accept an offer & end negotiations, return the same offer
        # Note: Store a copy of whatever offer you make in self.offer at the end of this method.
    def make_offer(self, offer):
        if offer:
            self.enemy_offer_history.append(offer)
            if self.goingFirst is None:
                self.goingFirst = False
                print("I'm not going first!")
        else:
            if self.goingFirst is None:
                self.goingFirst = True
                print("I'm going first!")


    def utility(self):
        utility = super().utility()
        self.my_utility_history.append(utility)
        return utility

    # receive_utility(self : BaseNegotiator, utility : Float)
        # Store the utility the other negotiator received from their last offer
    def receive_utility(self, utility):
        self.enemy_utility_history.append(utility)

    # receive_results(self : BaseNegotiator, results : (Boolean, Float, Float, Int))
        # Store the results of the last series of negotiation (points won, success, etc.)
    def receive_results(self, results):
        #Always from the same opponent
        self.results.append(results)

    def total_score(self):
        return sum(self.utility_received)