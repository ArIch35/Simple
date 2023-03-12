from collision_testing import *
from math import *

class TestBounce(BasicMovement):
    def __init__(self,window_size,constant_gravity=10,constant_x=5,angle=30):
        super().__init__(window_size,(0,0,0),constant_gravity,constant_x)
        self.angle = angle

    @override
    def key_control(self, event):
        pass

    @override
    def move(self, obj):
        obj.move_y_axis()


    @override
    def physics_control(self,ply):
        old_x,old_y = ply.position.get_x_y_position()

        if ply.collision_detector.window_cd.colliding_floor:
            ply.set_gravity(abs(ply.gravity_modifier)*-1)
            ply.collision_detector.has_collided = True

        if ply.collision_detector.window_cd.colliding_celling:
            ply.set_gravity(abs(ply.gravity_modifier))
            ply.collision_detector.has_collided = True

        if ply.collision_detector.window_cd.colliding_right_wall:
            ply.set_x_modifier(abs(ply.x_modifier) * -1)
            ply.collision_detector.has_collided = True

        if ply.collision_detector.window_cd.colliding_left_wall:
            ply.set_x_modifier(abs(ply.x_modifier))
            ply.collision_detector.has_collided = True

        if ply.collision_detector.has_collided:
            new_x = old_x + (ply.x_modifier * cos(self.angle))
            new_y = old_y + (ply.gravity_modifier * cos(self.angle))
            ply.set_position(ply.collision_detector.window_cd.control_wall_collision(Position(new_x, new_y)))

        for obj in self.movable_objects_list:
            obj_x, obj_y = obj.position.get_x_y_position()
            ply_x, ply_y = ply.position.get_x_y_position()
            if obj == ply:
                continue
            if ply.collision_detector.object_cd.check_collision_with_object(ply,obj):
                ply.set_position(Position(ply_x + abs(ply.x_modifier), ply_y + abs(ply.gravity_modifier)))
                obj.set_position(Position(obj_x - abs(obj.x_modifier), obj_y - abs(obj.gravity_modifier)))



#main 1
tst = TestBounce(Size(700,500),10,20,30)
tst.add_object(Rectangle("1",60,60,30,30,(255,0,0)))
tst.add_object(Rectangle("2",120,60,30,30,(255,0,0)))
tst.add_object(Rectangle("3",120,200,30,30,(255,0,0)))
tst.add_object(Rectangle("4",180,60,30,30,(255,0,0)))
tst.add_object(Rectangle("5",240,60,30,30,(255,0,0)))
tst.add_object(Rectangle("1",270,60,30,30,(255,0,0)))
tst.add_object(Rectangle("2",300,60,30,30,(255,0,0)))
tst.add_object(Rectangle("3",330,200,30,30,(255,0,0)))
tst.add_object(Rectangle("4",360,60,30,30,(255,0,0)))
tst.add_object(Rectangle("5",390,60,30,30,(255,0,0)))


tst.show()
