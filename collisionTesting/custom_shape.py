import pygame


class Position:
    def __init__(self, pos_x, pos_y):
        self.post_x = pos_x
        self.post_y = pos_y

    def get_x_pos(self):
        return self.post_x

    def get_y_pos(self):
        return self.post_y

    def get_position(self):
        return self.post_x, self.post_y

    def __repr__(self):
        return f"x = {self.post_x}, y = {self.post_y}"

    def __eq__(self, other):
        tar_x, tar_y = other.get_position()
        if (self.post_x == tar_x) & (self.post_y == tar_y):
            return True
        return False


class Size:
    def __init__(self, width, length):
        self.width = width
        self.length = length

    def get_width(self):
        return self.width

    def get_length(self):
        return self.length

    def get_size(self):
        return self.width, self.length

    def __eq__(self, other):
        tar_w, tar_l = other.get_position()
        if (self.width == tar_w) & (self.length == tar_l):
            return True
        return False

    def __repr__(self):
        return f"w = {self.width}, l = {self.length}"


# container for shape class
class ShapeType:
    def __init__(self, object_type, object_id, shape, color, position):
        self.type = object_type
        self.ID = object_id
        self.shape = shape
        self.color = color
        self.position = position
        self.size = Size(self.shape.width, self.shape.height)
        self.window = None
        self.colliding_celling = False
        self.colliding_floor = False
        self.colliding_right_wall = False
        self.colliding_left_wall = False
        self.object_has_collided = False

    def set_y_axis_collision_flag(self, celling_flag, floor_flag):
        self.colliding_celling = celling_flag
        self.colliding_floor = floor_flag

    def set_x_axis_collision_flag(self, right_wall_flag, left_wall_flag):
        self.colliding_right_wall = right_wall_flag
        self.colliding_left_wall = left_wall_flag

    def control_y_wall_collision(self, new_pos):
        colliding_with_floor = new_pos.get_y_pos() >= self.window.get_screen_width() - self.get_shape_size().get_length()

        if colliding_with_floor:
            print("s")
            self.set_y_axis_collision_flag(False, True)
            self.set_position(Position(new_pos.get_x_pos(), self.window.get_screen_width() - self.get_shape_size().get_length()))
            return

        colliding_with_celling = new_pos.get_y_pos() <= 0
        if colliding_with_celling:
            self.set_y_axis_collision_flag(True, False)
            self.set_position(Position(new_pos.get_x_pos(), 0))
            return

        self.set_y_axis_collision_flag(False, False)
        self.set_position(new_pos)

    def control_x_wall_collision(self, new_pos):
        colliding_with_right_wall = new_pos.get_x_pos() >= self.window.get_screen_height() - \
                                    self.size.get_width()

        if colliding_with_right_wall:
            self.set_x_axis_collision_flag(True, False)
            self.set_position(Position(self.window.get_screen_height() - self.get_shape_size().get_width(), new_pos.get_y_pos()))
            return

        colliding_with_left_wall = new_pos.get_x_pos() <= 0
        if colliding_with_left_wall:
            self.set_x_axis_collision_flag(False, True)
            self.set_position(Position(0, new_pos.get_y_pos()))
            return

        self.set_x_axis_collision_flag(False, False)
        self.set_position(new_pos)

    def control_wall_collision(self, new_pos):
        self.control_y_wall_collision(new_pos)
        self.control_x_wall_collision(self.position)

    def check_object_x_collision(self, tar, margin=0):
        ply_x, ply_y = self.get_median_position().get_position()
        obj_x, obj_y = tar.get_median_position().get_position()

        # divided by 2 to consider the space between 2 object
        margin /= 2

        src_tar_left_collision = obj_x + tar.get_shape_size().get_width() >= ply_x + margin
        src_tar_right_collision = obj_x + margin <= ply_x + self.get_shape_size().get_width()

        if src_tar_left_collision and src_tar_right_collision:
            return True
        return False

    def check_object_y_collision(self, tar, margin=0):
        ply_x, ply_y = self.get_median_position().get_position()
        obj_x, obj_y = tar.get_median_position().get_position()

        # divided by 2 to consider the space between 2 object
        margin /= 2

        src_tar_buttom_collision = obj_y + tar.get_shape_size().get_length() >= ply_y + margin
        src_tar_top_collision = obj_y + margin <= ply_y + self.get_shape_size().get_length()

        if src_tar_buttom_collision and src_tar_top_collision:
            return True
        return False

    # margin is minus for further collision, plus to allow some more collision
    def check_collision_with_object(self, tar, margin=0):
        if self.check_object_x_collision(tar, margin) and self.check_object_y_collision(tar, margin):
            return True
        return False

    def get_type(self):
        return self.type

    def get_id(self):
        return self.ID

    def get_object(self):
        return self.shape, self.color

    def get_shape_position(self):
        return self.position

    def get_shape_size(self):
        return self.size

    def has_collided(self):
        return self.object_has_collided

    def set_window(self, window):
        self.window = window

    def set_has_collided(self, value):
        self.object_has_collided = value

    def get_median_position(self):
        pos_x, pos_y = self.get_shape_position().get_position()
        obj_width, obj_length = self.get_shape_size().get_size()
        median_pos_x = pos_x + obj_width // 2
        median_pos_y = pos_y - obj_length // 2
        return Position(median_pos_x, median_pos_y)

    def set_color(self, color):
        self.color = color

    def set_position(self, new_position):
        self.position = new_position
        self.shape.x, self.shape.y = new_position.get_position()

    def __eq__(self, other):
        tar_id = other.get_id()
        if tar_id == self.ID:
            return True
        return False

    def __repr__(self):
        return f"type: {self.type}, ID: {self.ID}, pos ({self.position.__repr__()}), size ({self.size.__repr__()}) , has_collided: " \
               f"{self.object_has_collided}"


class Rectangle(ShapeType):
    def __init__(self, object_id, pos_x, pos_y, width, height, color=None):
        super().__init__("Rect", object_id, pygame.Rect(pos_x, pos_y, width, height), color, Position(pos_x, pos_y))


class ShapeDecider:
    def __init__(self, window):
        self.window = window

    def decide_shape(self, obj):
        obj_concrete, obj_color = obj.get_object()
        if obj.get_type() == "Rect":
            return pygame.draw.rect(self.window, obj_color, obj_concrete)