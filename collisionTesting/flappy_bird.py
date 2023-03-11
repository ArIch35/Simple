import random

from simple_collision import *

class FlappyBird(BasicMovement):
    FLYING_CONSTANT = 0.9
    FALLING_CONSTANT = 0.05

    def __init__(self, height, width, color,total_object = 5, gravity=10, x_modifier=10):
        super().__init__(height, width, color, gravity, x_modifier)
        self.space_is_clicked = False
        self.y_modifier = 0
        self.total_random_object = total_object
        self.composite_id = 0

    def add_altitude(self):
        self.y_modifier += self.FLYING_CONSTANT * self.gravity_modifier

    def reduce_altitude(self):
        self.y_modifier -= self.gravity_modifier * self.FALLING_CONSTANT

    def set_altitude(self, value):
        self.y_modifier = value

    @override
    def key_control(self, event):
        if event.key == pygame.K_SPACE:
            self.space_is_clicked = True
        if event.key == pygame.K_RIGHT:
            self.change_x_modifier(abs(self.x_modifier))
        if event.key == pygame.K_LEFT:
            self.change_x_modifier(abs(self.x_modifier) * -1)

    @override
    def move(self, obj):
        old_x, old_y = obj.get_shape_position().get_position()

        self.fly_control(obj)

        # check if moving in x-axis
        if self.x_is_moving is False:
            return Position(old_x, old_y - self.y_modifier)
        return Position(old_x + self.x_modifier, old_y)

    def fly_control(self,obj):
        if obj.colliding_celling or obj.colliding_floor:
            self.set_altitude(0)
        if self.space_is_clicked:
            self.space_is_clicked = False
            self.add_altitude()
        else:
            self.reduce_altitude()

    @override
    def physics_control(self, ply):
        for obj in self.immovable_objects_list:
            if ply.check_collision_with_object(obj):
                self.remove_object(self.get_object_by_list(obj, self.immovable_objects_list),
                                   self.immovable_objects_list)
        if len(self.immovable_objects_list) < self.total_random_object:
            size = (obj.get_shape_size().get_width() + obj.get_shape_size().get_length()) // 2
            self.generate_random_immovable_object(size, abs(len(self.immovable_objects_list) - self.total_random_object))
            self.update_immovable_object()

    def generate_random_immovable_object(self, immovable_object_size=10, immovable_object_total=5):
        grid_width = (self.screen_width - self.MARGIN_BORDER) // immovable_object_size - 1
        grid_height = (self.screen_height - self.MARGIN_BORDER) // immovable_object_size - 1
        for i in range(immovable_object_total):
            obj_id = f"OBJECT_{self.composite_id}"
            rand_x = random.randint(1, grid_height)
            rand_y = random.randint(1, grid_width)

            final_x_pos = (rand_x + 1) * immovable_object_size
            final_y_pos = (rand_y + 1) * immovable_object_size

            if self.get_object_by_pos(Position(final_x_pos, final_y_pos), self.immovable_objects_list) is not None:
                while self.get_object_by_pos(Position(final_x_pos, final_y_pos), self.immovable_objects_list).get_id().find("OBJECT") == 1:
                    rand_x = random.randint(1, grid_width) + self.MARGIN_BORDER
                    rand_y = random.randint(1, grid_height) + self.MARGIN_BORDER

            final_x_pos = (rand_x + 1) * immovable_object_size
            final_y_pos = (rand_y + 1) * immovable_object_size

            self.add_immovable_object(Rectangle(obj_id, final_x_pos, final_y_pos, immovable_object_size,
                                                immovable_object_size, (0, 255, 0)))
            self.composite_id += 1

#main
tst = FlappyBird(300,400,(0,0,0),5,8,20)
tst.set_FPS(30)
tst.add_object(Rectangle("PLAYER1",0,320,30,60,(255,0,0)))
tst.generate_random_immovable_object(10,1)
tst.show()
