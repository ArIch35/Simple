import pygame
from custom_shape import *
from size import *
from pygame.locals import *
import sys

def override(function):
    return function


class Window:

    # Window is basically a abstract class that connect all other class and provide only the window.
    # When you want to create something just inherit it from window and override show().

    def __init__(self, window_size, color):
        self.window_size = window_size
        self.surface = pygame.display.set_mode((window_size.width, window_size.length))
        self.background_color = color
        self.shape_decider = ShapeDecider(self.surface)
        self.clock = pygame.time.Clock()
        self.movable_objects_list = []
        self.immovable_objects_list = []
        self.FPS = 30

    def get_object_by_pos(self, pos, list_type):
        for obj in list_type:
            if obj.position == pos:
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
        for index, obj in enumerate(self.movable_objects_list):
            median_width += obj.get_shape_size().get_width()
            median_length += obj.get_shape_size().get_length()
        count_obj = index + 1
        return Size(median_width // count_obj, median_length // count_obj)

    def add_object(self, shape):
        self.movable_objects_list.append(shape)
        shape.set_collision_detector(self.window_size)

    def add_immovable_object(self, shape):
        self.immovable_objects_list.append(shape)

    def set_objects_to_correct_shape(self):
        for obj in self.movable_objects_list:
            self.shape_decider.decide_shape(obj)
        for obj in self.immovable_objects_list:
            self.shape_decider.decide_shape(obj)

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
    def __init__(self, window_size, color, gravity=5, x_modifier=20):
        super().__init__(window_size, color)
        self.MARGIN_BORDER = 40
        self.object_gravity = gravity
        self.object_x_modifier = x_modifier
        self.x_is_moving = False

    def set_gravity(self, value):
        self.object_gravity = value

        for obj in self.movable_objects_list:
            obj.set_gravity(value)

    def set_x_modifier(self, value):
        self.object_x_modifier = value
        self.x_is_moving = True

        for obj in self.movable_objects_list:
            obj.set_x_modifier(value)

    @override
    def add_object(self, shape):
        shape.set_gravity(self.object_gravity)
        shape.set_x_modifier(self.object_x_modifier)
        self.movable_objects_list.append(shape)
        shape.set_collision_detector(self)

    def set_border_margin(self, value):
        self.MARGIN_BORDER = value

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
        for obj in self.movable_objects_list:
            self.move(obj)
            self.physics_control(obj)
            self.shape_decider.decide_shape(obj)

    def move(self, obj):
        # check if moving in x-axis
        if self.x_is_moving is False:
            obj.move_x_axis()
            return
        obj.move_y_axis()

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

    def key_control(self, event, obj):
        if event.key == pygame.K_SPACE:
            obj.set_gravity(obj.gravity_modifier * -1)
        if event.key == pygame.K_RIGHT:
            obj.set_x_modifier(abs(obj.gravity_modifier))
        if event.key == pygame.K_LEFT:
            obj.set_x_modifier(abs(obj.gravity_modifier) * -1)

    # returns Position Object
    def physics_control(self, ply):
        pass
