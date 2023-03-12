import random

from custom_window import *

class FlappyBird(BasicMovement):
    FLYING_CONSTANT = 5
    FALLING_CONSTANT = 0.05

    def __init__(self, window_size, color, total_object = 5, gravity=10, x_modifier=10):
        super().__init__(window_size, color, gravity, x_modifier)
        self.space_is_clicked = False
        self.y_modifier = 0
        self.total_random_object = total_object
        self.composite_id = 0

    def add_altitude(self):
        self.y_modifier += self.FLYING_CONSTANT * self.object_gravity

    def reduce_altitude(self):
        self.y_modifier -= self.object_gravity * self.FALLING_CONSTANT

    def set_altitude(self, value):
        self.y_modifier = value

    @override
    def key_control(self, event):
        if event.key == pygame.K_SPACE:
            self.space_is_clicked = True
        if event.key == pygame.K_RIGHT:
            self.set_x_modifier(abs(self.object_x_modifier))
        if event.key == pygame.K_LEFT:
            self.set_x_modifier(abs(self.object_x_modifier) * -1)

    @override
    def move(self, obj):
        self.fly_control(obj)

        # check if moving in x-axis
        if self.x_is_moving is False :
            obj.move_y_axis(5)
            return
        obj.move_x_axis()

    def fly_control(self,obj):
        if obj.collision_detector.window_cd.colliding_celling or obj.collision_detector.window_cd.colliding_floor:
            self.set_altitude(0)
        if self.space_is_clicked:
            self.space_is_clicked = False
            self.add_altitude()
            obj.move_y_axis(abs(self.y_modifier) * -1)
        else:
            self.reduce_altitude()

    @override
    def physics_control(self, ply):
        for obj in self.immovable_objects_list:
            if ply.collision_detector.object_cd.check_collision_with_object(ply,obj):
                self.remove_object(self.get_object_by_list(obj, self.immovable_objects_list),
                                   self.immovable_objects_list)
        if len(self.immovable_objects_list) < self.total_random_object:
            size = (obj.size.width + obj.size.length) // 2
            self.generate_random_immovable_object(size, abs(len(self.immovable_objects_list) - self.total_random_object))
            self.update_immovable_object()

    def generate_random_immovable_object(self, immovable_object_size=10, immovable_object_total=5):
        grid_width = (self.window_size.width - self.MARGIN_BORDER) // immovable_object_size - 1
        grid_height = (self.window_size.length - self.MARGIN_BORDER) // immovable_object_size - 1
        for i in range(immovable_object_total):
            obj_id = f"OBJECT_{self.composite_id}"
            rand_x = random.randint(1, grid_height)
            rand_y = random.randint(1, grid_width)

            final_x_pos = (rand_x + 1) * immovable_object_size
            final_y_pos = (rand_y + 1) * immovable_object_size

            if self.get_object_by_pos(Position(final_x_pos, final_y_pos), self.immovable_objects_list) is not None:
                while self.get_object_by_pos(Position(final_x_pos, final_y_pos), self.immovable_objects_list).ID.find("OBJECT") == 1:
                    rand_x = random.randint(1, grid_width) + self.MARGIN_BORDER
                    rand_y = random.randint(1, grid_height) + self.MARGIN_BORDER

            final_x_pos = (rand_x + 1) * immovable_object_size
            final_y_pos = (rand_y + 1) * immovable_object_size

            self.add_immovable_object(Rectangle(obj_id, final_x_pos, final_y_pos, immovable_object_size,
                                                immovable_object_size, (0, 255, 0)))
            self.composite_id += 1

#main
tst = FlappyBird(Size(300,400),(0,0,0),10,8,20)
tst.set_FPS(30)
tst.add_object(Rectangle("PLAYER1",0,320,30,60,(255,0,0)))
tst.generate_random_immovable_object(10,11)
tst.show()
