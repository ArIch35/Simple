from position import *


class CollisionDetector:
    def __init__(self, window, shape):
        self.has_collided = False
        self.window_cd = WindowCollisionDetector(window, shape)
        self.object_cd = ObjectCollisionDetector()


class WindowCollisionDetector:

    def __init__(self, window, shape):
        self.window_size = window.window_size
        self.object_size = shape.size
        self.colliding_celling = False
        self.colliding_floor = False
        self.colliding_right_wall = False
        self.colliding_left_wall = False

    def collide_window_in_y_axis(self):
        return self.colliding_celling or self.colliding_floor

    def collide_window_in_x_axis(self):
        return self.colliding_right_wall or self.colliding_left_wall

    def collided_with_window(self):
        return self.collide_window_in_y_axis() or self.collide_window_in_x_axis()

    def set_x_axis_collision_flag(self, right_wall_flag, left_wall_flag):
        self.colliding_right_wall = right_wall_flag
        self.colliding_left_wall = left_wall_flag

    def set_y_axis_collision_flag(self, celling_flag, floor_flag):
        self.colliding_celling = celling_flag
        self.colliding_floor = floor_flag

    def detect_floor_collision(self, object_position):
        return object_position.post_y >= self.window_size.length - self.object_size.length

    def detect_celling_collision(self, object_position):
        return object_position.post_y <= 0

    def detect_right_window_collision(self, object_position):
        return object_position.post_x >= self.window_size.width - self.object_size.width

    def detect_left_window_collision(self, object_position):
        return object_position.post_x <= 0

    def correct_floor_collision(self, object_position):
        self.set_y_axis_collision_flag(False, True)
        return Position(object_position.post_x, self.window_size.length - self.object_size.length)

    def correct_celling_collision(self, object_position):
        self.set_y_axis_collision_flag(True, False)
        return Position(object_position.post_x, 0)

    def correct_right_window_collision(self, object_position):
        self.set_x_axis_collision_flag(True, False)
        return Position(self.window_size.width - self.object_size.width, object_position.post_y)

    def correct_left_window_collision(self, object_position):
        self.set_x_axis_collision_flag(False, True)
        return Position(0, object_position.post_y)

    def control_x_wall_collision(self, object_position):
        if self.detect_right_window_collision(object_position):
            return self.correct_right_window_collision(object_position)

        if self.detect_left_window_collision(object_position):
            return self.correct_left_window_collision(object_position)

        self.set_x_axis_collision_flag(False, False)
        return object_position

    def control_y_wall_collision(self, object_position):
        if self.detect_floor_collision(object_position):
            return self.correct_floor_collision(object_position)

        if self.detect_celling_collision(object_position):
            return self.correct_celling_collision(object_position)

        self.set_y_axis_collision_flag(False, False)
        return object_position

    def control_wall_collision(self, object_position):
        object_position = self.control_y_wall_collision(object_position)
        return self.control_x_wall_collision(object_position)


class ObjectCollisionDetector:

    def detect_right_side_collision(self, margin, source, destination):
        return source.position.post_x + margin <= destination.position.post_x + destination.size.width

    def detect_left_side_collision(self, margin, source, destination):
        return source.position.post_x + source.size.width >= destination.position.post_x + margin

    def detect_bottom_side_collision(self, margin, source, destination):
        return source.position.post_y + source.size.length >= destination.position.post_y + margin

    def detect_top_side_collision(self, margin, source, destination):
        return source.position.post_y + margin <= destination.position.post_y + destination.size.length

    def check_object_x_collision(self, source, destination, margin=0):
        margin /= 2

        if self.detect_left_side_collision(margin, source, destination) \
                and self.detect_right_side_collision(margin, source, destination):
            return True
        return False

    def check_object_y_collision(self, source, destination, margin=0):
        # divided by 2 to consider the space between 2 object
        margin /= 2

        if self.detect_bottom_side_collision(margin, source, destination) and \
                self.detect_top_side_collision(margin, source, destination):
            return True
        return False

    # margin is minus for further collision, plus to allow some more collision
    def check_collision_with_object(self, source, destination, margin=0):
        if self.check_object_x_collision(source, destination, margin) and \
                self.check_object_y_collision(source, destination, margin):
            return True
        return False
