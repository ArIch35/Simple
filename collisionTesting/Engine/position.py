class Position:
    def __init__(self, pos_x, pos_y):
        self.post_x = pos_x
        self.post_y = pos_y

    def get_x_y_position(self):
        return self.post_x, self.post_y

    def get_median_position(self, object_size):
        obj_width, obj_length = object_size.get_both_size()
        median_pos_x = self.post_x + obj_width // 2
        median_pos_y = self.post_y - obj_length // 2
        return median_pos_x, median_pos_y

    def __repr__(self):
        return f"x = {self.post_x}, y = {self.post_y}"

    def __eq__(self, other):
        tar_x, tar_y = other.get_x_y_position()
        if (self.post_x == tar_x) & (self.post_y == tar_y):
            return True
        return False
