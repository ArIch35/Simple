from simple_collision import *

class TestGravity(BasicMovement):
    def __init__(self,width,length,constant_gravity=10,constant_x=5):
        super().__init__(width,length,(0,0,0))

    @override
    def physics_control(self, ply):
        for obj in self.objects_list:
            old_x, old_y = obj.get_shape_position().get_position()
            if obj == ply:
                continue
            if ply.check_collision_with_object(obj):
                obj.set_position(Position(old_x - self.x_modifier, old_y + self.gravity_modifier))

#main 1
tst = TestGravity(300,400,5,20)
tst.add_object(Rectangle("1",0,60,30,60,(255,0,0)))
tst.add_object(Rectangle("2",60,60,30,60,(255,0,0)))
tst.add_object(Rectangle("3",120,60,30,60,(255,0,0)))
tst.add_object(Rectangle("4",180,180,30,60,(255,0,0)))

tst.show()
