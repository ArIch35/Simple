class Size:
    def __init__(self, width, length):
        self.width = width
        self.length = length

    def get_both_size(self):
        return self.width, self.length

    def __eq__(self, other):
        tar_w, tar_l = other.get_position()
        if (self.width == tar_w) & (self.length == tar_l):
            return True
        return False

    def __repr__(self):
        return f"w = {self.width}, l = {self.length}"
