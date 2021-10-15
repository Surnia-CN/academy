package com.Surnia.eduService.controller;

import com.Surnia.commons.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName EduLoginController
 * @Description TODO
 * @Author Surnia
 * @Date 2021/6/26
 * @Version 1.0
 */

@CrossOrigin // 解决前后端端口不一致的跨域问题
@Api(tags = "登录管理")
@RestController
@RequestMapping("/eduService/eduLogin")
public class EduLoginController {

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(/*@RequestBody String username, @RequestBody String password*/) {
        //if (username != null && password != null) {
            return Result.ok().data("token", "admin");
        //} else {
        //    return Result.error();
        //}
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("getInfo")
    public Result getInfo() {
        return Result.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://th.bing.com/th/id/R42b0ffd25b6dd1f691b2c3f06d47855d?rik=qC9nLYDE%2bvslOQ&riu=http%3a%2f%2fwww.gx8899.com%2fuploads%2fallimg%2f2018041208%2fvag5jehfucx.jpg&ehk=0JJQ0L%2fLkYS40R0ffPFi0iyGwCYwAnIhWHQOP6OcXXc%3d&risl=&pid=ImgRaw");
    }


}
