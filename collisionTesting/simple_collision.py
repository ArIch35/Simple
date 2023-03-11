import sys
from pygame.locals import *
from custom_shape import *


def override(function):
    return function


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

    def get_screen_height(self):
        return self.screen_height

    def get_screen_width(self):
        return self.screen_width

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
        shape.set_window(None)

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
        shape.set_window(self)

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
        self.MARGIN_BORDER = 40

    def set_border_margin(self, value):
        self.MARGIN_BORDER = value

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
            obj.control_wall_collision(new_pos)
            self.physics_control(obj)
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
    def physics_control(self, ply):
        pass
