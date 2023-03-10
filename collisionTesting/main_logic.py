import pygame,sys
from pygame.locals import *

FPS = 30
PIXEL_UNTIL_COLLISION = 5

def override(function):
    return function

class Position:
    def __init__(self,pos_x,pos_y):
        self.post_x = pos_x
        self.post_y = pos_y

    def get_x_pos(self):
        return self.post_x

    def get_y_pos(self):
        return self.post_y

    def get_position(self):
        return self.post_x,self.post_y

    def __repr__(self):
        return f"x = {self.post_x}, y = {self.post_y}"

    def __eq__(self, other):
        tar_x,tar_y = other.get_position()
        if (self.post_x == tar_x) & (self.post_y == tar_y):
            return True
        return False

class Size:
    def __init__(self,width,length):
        self.width = width
        self.length = length

    def get_width(self):
        return self.width

    def get_length(self):
        return self.length

    def get_size(self):
        return self.width,self.length

    def __eq__(self, other):
        tar_w,tar_l = other.get_position()
        if (self.width == tar_w) & (self.length == tar_l):
            return True
        return False

    def __repr__(self):
        return f"w = {self.width}, l = {self.length}"

#container for shape class
class ShapeType:
    def __init__(self,type,ID,shape,color,position):
        self.type = type
        self.ID = ID
        self.shape = shape
        self.color = color
        self.position = position
        self.size = Size(self.shape.width,self.shape.height)

    def get_type(self):
        return self.type

    def get_id(self):
        return self.ID

    def get_object(self):
        return self.shape,self.color

    def get_shape_position(self):
        return self.position

    def get_shape_size(self):
        return self.size

    def get_median_position(self):
        pos_x, pos_y = self.get_shape_position().get_position()
        obj_width, obj_length = self.get_shape_size().get_size()
        median_pos_x = pos_x - obj_width // 2
        median_pos_y = pos_y - obj_length // 2
        return Position(median_pos_x,median_pos_y)

    def set_color(self,color):
        self.color = color

    def set_position(self, new_position):
        self.position = new_position
        self.shape.x,self.shape.y = new_position.get_position()

    def __eq__(self, other):
        tar_id = other.get_id()
        if tar_id == self.ID:
            return True
        return False

    def __repr__(self):
        return f"type: {self.type}, ID: {self.ID}, pos ({self.position.__repr__}), size ({self.size.__repr__()})"


class Rectangle(ShapeType):
    def __init__(self,ID,pos_x,pos_y,width,height,color = None):
        super().__init__("Rect",ID,pygame.Rect(pos_x,pos_y,width,height),color,Position(pos_x,pos_y))

class ShapeDecider:
    def __init__(self,window):
        self.window = window

    def decide_shape(self,obj):
        obj_concrete,obj_color = obj.get_object()
        if obj.get_type() == "Rect":
            return pygame.draw.rect(self.window,obj_color,obj_concrete)


class Window:
    # Window is basically a abstract class that connect all other class and provide only the window.
    # When you want to create smth just inherit it from window and override show().

    def __init__(self,height,width,color):
        self.screen_height = height
        self.screen_width = width
        self.surface = pygame.display.set_mode((height,width))
        self.background_color = color
        self.shape_decider = ShapeDecider(self.surface)
        self.clock = pygame.time.Clock()
        self.objects_list = []
        self.objects_median_position = []

    def get_object_at(self,tar):
        return filter(lambda obj: obj.get_id() == tar,self.objects_list)

    def get_all_objects_size_mean(self):
        median_width = 0
        median_length = 0
        for index,obj in enumerate(self.objects_list):
            median_width += obj.get_shape_size().get_width()
            median_length += obj.get_shape_size().get_length()
        count_obj = index + 1
        return Size(median_width//count_obj,median_length//count_obj)

    def add_object(self,shape):
        self.objects_list.append(shape)
        self.objects_median_position.append(shape.get_median_position())

    def set_objects_to_correct_shape(self):
        for obj in self.objects_list:
            self.shape_decider.decide_shape(obj)

    def set_all_median_position(self):
        self.objects_median_position.clear()
        for obj in self.objects_list:
            pos_x,pos_y = obj.get_median_position().get_median_position()
            self.objects_median_position.append(Position(pos_x,pos_y))

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




#provide basic movement with wall collision
#if want to create custom movement, override movement_control() and of course physics_control(), other are not necessary
class BasicMovement(Window):

    def __init__(self,height,width,color,gravity=5,x_modifier=20):
        super().__init__(height,width,color)
        self.gravity_modifier = gravity
        self.x_modifier = x_modifier
        self.x_is_moving = False

    def check_y_wall_collision(self, new_pos, obj, old_x):
        colliding_with_floor = new_pos.post_y >= self.screen_width - obj.get_shape_size().get_length()
        if colliding_with_floor:
            new_pos = Position(old_x, self.screen_width - obj.get_shape_size().get_length())

        colliding_with_celling = new_pos.post_y <= 0
        if colliding_with_celling:
            new_pos = Position(old_x, 0)
        return new_pos

    def check_x_wall_collision(self, new_pos, obj, old_y):
        colliding_with_right_wall = new_pos.post_x >= self.screen_height - obj.get_shape_size().get_width()
        if colliding_with_right_wall:
            new_pos = Position(self.screen_height - obj.get_shape_size().get_width(), old_y)

        colliding_with_left_wall = new_pos.post_x <= 0
        if colliding_with_left_wall:
            new_pos = Position(0, old_y)
        return new_pos

    def detect_wall_collision(self, obj):
        old_x, old_y = obj.get_shape_position().get_position()
        new_pos = Position(old_x, old_y + self.gravity_modifier)
        new_pos = self.check_y_wall_collision(new_pos, obj, old_x)

        # check if moving in x-axis
        if self.x_is_moving is False:
            return new_pos

        new_pos = Position(old_x + self.x_modifier, old_y)
        new_pos = self.check_x_wall_collision(new_pos, obj, old_y)

        return new_pos

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
            self.clock.tick(FPS)
            pygame.display.update()

    def update_position(self):
        self.surface.fill(self.background_color)
        for obj in self.objects_list:
            new_pos = self.detect_wall_collision(obj)
            new_pos = self.physics_control(new_pos,obj)
            obj.set_position(new_pos)
            self.shape_decider.decide_shape(obj)
        self.x_is_moving = False

    def movement_control(self):
            for event in pygame.event.get():
                if event.type == QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.KEYDOWN:
                    self.key_control(event)

    def key_control(self, event):
        if event.key == pygame.K_SPACE:
            self.change_gravity(self.gravity_modifier * -1)
        if event.key == pygame.K_RIGHT:
            self.change_x_modifier(abs(self.x_modifier))
        if event.key == pygame.K_LEFT:
            self.change_x_modifier(abs(self.x_modifier) * -1)

    def physics_control(self, new_pos,obj):
        return new_pos




#main2
tst = BasicMovement(300,400,(0,0,0))
tst.add_object(Rectangle("1",30,60,30,60,(255,0,0)))
tst.show()
