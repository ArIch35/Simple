import pygame
import sys
from pygame.locals import *


def override(function):
    return function


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
        return f"type: {self.type}, ID: {self.ID}, pos ({self.position.__repr__}), size ({self.size.__repr__()})"


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


class Window:

    # Window is basically a abstract class that connect all other class and provide only the window.
    # When you want to create something just inherit it from window and override show().

    def __init__(self, height, width, color):
        self.screen_height = height
        self.screen_width = width
        self.surface = pygame.display.set_mode((height, width))
        self.background_color = color
        self.shape_decider = ShapeDecider(self.surface)
        self.clock = pygame.time.Clock()
        self.objects_list = []
        self.immovable_objects_list = []
        self.objects_median_position = []
        self.FPS = 30

    def get_object_by_pos(self, pos, list_type):
        for obj in list_type:
            if obj.get_shape_position() == pos:
                return obj
        return None

    def get_object_by_list(self, tar, list_type):
        for obj in list_type:
            if obj == tar:
                return obj
        return None

    def remove_object(self, shape, list_type):
        list_type.remove(shape)

    def get_all_objects_size_mean(self):
        median_width = 0
        median_length = 0
        for index, obj in enumerate(self.objects_list):
            median_width += obj.get_shape_size().get_width()
            median_length += obj.get_shape_size().get_length()
        count_obj = index + 1
        return Size(median_width // count_obj, median_length // count_obj)

    def add_object(self, shape):
        self.objects_list.append(shape)
        self.objects_median_position.append(shape.get_median_position())

    def add_immovable_object(self, shape):
        self.immovable_objects_list.append(shape)

    def set_objects_to_correct_shape(self):
        for obj in self.objects_list:
            self.shape_decider.decide_shape(obj)
        for obj in self.immovable_objects_list:
            self.shape_decider.decide_shape(obj)

    def set_all_median_position(self):
        self.objects_median_position.clear()
        for obj in self.objects_list:
            pos_x, pos_y = obj.get_median_position().get_median_position()
            self.objects_median_position.append(Position(pos_x, pos_y))

    def initialize_window(self):
        pygame.init()
        self.set_objects_to_correct_shape()
        pygame.display.flip()

    def show(self):
        self.initialize_window()

        while True:
            for event in pygame.event.get():
                if event.type == QUIT:
                    pygame.quit()
                    sys.exit()
            pygame.display.update()

    def set_FPS(self, fps):
        self.FPS = fps


# provide basic movement with wall collision
# if want to create custom movement, override movement_control(),move() and physics_control(), other are not necessary
class BasicMovement(Window):
    def __init__(self, height, width, color, gravity=5, x_modifier=20):
        super().__init__(height, width, color)
        self.gravity_modifier = gravity
        self.x_modifier = x_modifier
        self.x_is_moving = False
        self.colliding_celling = False
        self.colliding_floor = False
        self.colliding_right_wall = False
        self.colliding_left_wall = False
        self.MARGIN_BORDER = 40

    def set_border_margin(self, value):
        self.MARGIN_BORDER = value

    def set_y_axis_collision_flag(self, celling_flag, floor_flag):
        self.colliding_celling = celling_flag
        self.colliding_floor = floor_flag

    def set_x_axis_collision_flag(self, right_wall_flag, left_wall_flag):
        self.colliding_right_wall = right_wall_flag
        self.colliding_left_wall = left_wall_flag

    def check_y_wall_collision(self, new_pos, obj, old_x):
        colliding_with_floor = new_pos.post_y >= self.screen_width - obj.get_shape_size().get_length()
        if colliding_with_floor:
            self.set_y_axis_collision_flag(False, True)
            return Position(old_x, self.screen_width - obj.get_shape_size().get_length())

        colliding_with_celling = new_pos.post_y <= 0
        if colliding_with_celling:
            self.set_y_axis_collision_flag(True, False)
            return Position(old_x, 0)

        self.set_y_axis_collision_flag(False, False)
        return new_pos

    def check_x_wall_collision(self, new_pos, obj, old_y):
        colliding_with_right_wall = new_pos.post_x >= self.screen_height - obj.get_shape_size().get_width()
        if colliding_with_right_wall:
            self.set_x_axis_collision_flag(True, False)
            return Position(self.screen_height - obj.get_shape_size().get_width(), old_y)

        colliding_with_left_wall = new_pos.post_x <= 0
        if colliding_with_left_wall:
            self.set_x_axis_collision_flag(False, True)
            return Position(0, old_y)

        self.set_x_axis_collision_flag(False, False)
        return new_pos

    def detect_wall_collision(self, obj, new_pos):
        old_x, old_y = obj.get_shape_position().get_position()

        if self.x_is_moving is False:
            return self.check_y_wall_collision(new_pos, obj, old_x)

        return self.check_x_wall_collision(new_pos, obj, old_y)

    def check_collision_with_object(self, src, tar, margin=0):
        ply_x, ply_y = src.get_median_position().get_position()
        obj_x, obj_y = tar.get_median_position().get_position()

        src_tar_left_collision = obj_x + tar.get_shape_size().get_width() >= ply_x + margin
        src_tar_right_collision = obj_x + margin <= ply_x + src.get_shape_size().get_width()
        src_tar_buttom_collision = obj_y + tar.get_shape_size().get_length() >= ply_y + margin
        src_tar_top_collision = obj_y + margin <= ply_y + src.get_shape_size().get_length()

        if src_tar_left_collision and src_tar_right_collision and src_tar_buttom_collision and src_tar_top_collision:
            return True
        return False

    def change_gravity(self, value):
        self.gravity_modifier = value

    def change_x_modifier(self, value):
        self.x_modifier = value
        self.x_is_moving = True

    @override
    def show(self):
        self.initialize_window()
        while True:
            self.movement_control()
            self.update_position()
            self.clock.tick(self.FPS)
            pygame.display.update()

    def update_position(self):
        self.surface.fill(self.background_color)
        self.update_movable_object()
        self.update_immovable_object()
        self.x_is_moving = False

    def update_movable_object(self):
        for obj in self.objects_list:
            new_pos = self.move(obj)
            new_pos = self.detect_wall_collision(obj, new_pos)
            new_pos = self.physics_control(new_pos, obj)
            obj.set_position(new_pos)
            self.shape_decider.decide_shape(obj)

    def update_immovable_object(self):
        for obj in self.immovable_objects_list:
            self.shape_decider.decide_shape(obj)

    def movement_control(self):
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                self.key_control(event)

    # returns Position Object
    def move(self, obj):
        old_x, old_y = obj.get_shape_position().get_position()
        # check if moving in x-axis
        if self.x_is_moving is False:
            return Position(old_x, old_y + self.gravity_modifier)
        return Position(old_x + self.x_modifier, old_y)

    def key_control(self, event):
        if event.key == pygame.K_SPACE:
            self.change_gravity(self.gravity_modifier * -1)
        if event.key == pygame.K_RIGHT:
            self.change_x_modifier(abs(self.x_modifier))
        if event.key == pygame.K_LEFT:
            self.change_x_modifier(abs(self.x_modifier) * -1)

    # returns Position Object
    def physics_control(self, new_pos,ply):
        return new_pos
