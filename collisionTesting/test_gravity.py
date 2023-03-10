class TestGravity(Window):
    def __init__(self,width,length,constant_gravity=10,constant_x=5):
        super().__init__(width,length,(0,0,0))
        self.gravity_constant = constant_gravity
        self.x_modifier = constant_x
        self.is_moving_x = False
        self.checked_object_colision = []

    @override
    def show(self):
        pygame.init()

        self.set_objects()
        self.set_abs_position()


        pygame.display.flip()

        while True:
            for event in pygame.event.get():
                if event.type == QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_SPACE:
                        self.change_gravity(self.gravity_constant * -1)
                    if event.key == pygame.K_RIGHT:
                        self.change_x_modifier(abs(self.x_modifier))
                    if event.key == pygame.K_LEFT:
                        self.change_x_modifier(abs(self.x_modifier) * -1)

            self.update_position()
            self.set_abs_position()
            self.clock.tick(FPS)
            pygame.display.update()


    def update_position(self):
        self.surface.fill(self.background_color)
        for obj in self.objects:
            new_pos = self.detect_wall_collision(obj)
            new_pos = self.pysichs_collision(new_pos,obj)
            obj.change_position(new_pos)
            self.shape_decider.decide_shape(obj)
        self.is_moving_x = False

    def pysichs_collision(self,new_pos,obj):
        old_x, old_y = obj.get_position().get_position()
        has_collision, modifier = self.check_object_x_collision(obj)
        if has_collision:
            return Position(old_x, old_y), False
        return new_pos


    def detect_wall_collision(self, obj):
        old_x, old_y = obj.get_position().get_position()
        new_pos = Position(old_x, old_y + self.gravity_constant)
        new_pos = self.check_y_wall_collision(new_pos, obj, old_x)

        #check if moving in x-axis
        if self.is_moving_x is False:
            return new_pos

        new_pos = Position(old_x + self.x_modifier, old_y)
        new_pos = self.check_x_wall_collision(new_pos, obj, old_y)

        return new_pos,False



    def check_object_x_collision(self, obj):
        for pos in self.objects_position:
            if obj.get_median_position().is_equal(pos) is False:
                obj_x, obj_y = self.get_object_at(obj.get_median_position()).get_position()
                tar_x, tar_y = pos.get_position()
                actual_dist = abs(obj_x - tar_x)

                print(obj_x, tar_x, actual_dist)
                if actual_dist == 0:

                    for x in self.objects:
                        if x.get_position().is_equal(pos):
                            self.objects.remove(x)


                    return True,self.x_modifier
        return False,0

    def clear_checked_list(self):
        if len(self.checked_object_colision) >= len(self.objects):
            self.checked_object_colision.clear()


    def change_gravity(self,value):
        self.gravity_constant = value

    def change_x_modifier(self,value):
        self.x_modifier = value
        self.is_moving_x = True


#main 1
#tst = TestGravity(300,400,5,20)
#tst.add_object(Rectangle(0,60,30,60,(255,0,0)))
#tst.add_object(Rectangle(60,180,30,60,(255,0,0)))
#tst.add_object(Rectangle(120,180,30,60,(255,0,0)))
#tst.add_object(Rectangle(180,180,30,60,(255,0,0)))
#tst.show()
