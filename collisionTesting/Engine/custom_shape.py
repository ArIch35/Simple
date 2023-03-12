import pygame
from size import *
from collision_detector import *

# container for shape class
class ShapeType:
    def __init__(self, object_type, object_id, shape, color, position):
        self.type = object_type
        self.ID = object_id
        self.shape = shape
        self.color = color
        self.position = position
        self.size = Size(self.shape.width, self.shape.height)
        self.gravity_modifier = 0
        self.x_modifier = 0
        self.collision_detector = None

    def move_x_axis(self, value=-1):
        if value == -1:
            value = self.x_modifier

        self.set_position(self.collision_detector.window_cd.control_x_wall_collision(
            Position(self.position.post_x + value, self.position.post_y)))

    def move_y_axis(self, value=-1):
        if value == -1:
            value = self.gravity_modifier

        self.set_position(self.collision_detector.window_cd.control_y_wall_collision(
            Position(self.position.post_x, self.position.post_y + value)))

    def get_concrete_shape(self):
        return self.shape, self.color

    def set_gravity(self, value):
        self.gravity_modifier = value

    def set_x_modifier(self, value):
        self.x_modifier = value

    def set_collision_detector(self, window):
        self.collision_detector = CollisionDetector(window, self)

    def set_color(self, color):
        self.color = color

    def set_position(self, new_position):
        self.position = new_position
        self.shape.x, self.shape.y = new_position.get_x_y_position()

    def __eq__(self, other):
        if other.ID == self.ID:
            return True
        return False

    def __repr__(self):
        return f"type: {self.type}, ID: {self.ID}, pos ({self.position.__repr__()}), " \
               f"size ({self.size.__repr__()}) , has_collided: " \
               f"{self.collision_detector.window_cd.collided_with_window()}"


class Rectangle(ShapeType):
    def __init__(self, object_id, pos_x, pos_y, width, height, color=None):
        super().__init__("Rect", object_id, pygame.Rect(pos_x, pos_y, width, height), color, Position(pos_x, pos_y))


class ShapeDecider:
    def __init__(self, window):
        self.window = window

    def decide_shape(self, obj):
        obj_concrete, obj_color = obj.get_concrete_shape()
        if obj.type == "Rect":
            return pygame.draw.rect(self.window, obj_color, obj_concrete)
