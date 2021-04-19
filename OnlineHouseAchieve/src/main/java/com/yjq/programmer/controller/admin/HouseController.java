package com.yjq.programmer.controller.admin;

import com.yjq.programmer.enums.UserRoleEnum;
import com.yjq.programmer.pojo.common.House;
import com.yjq.programmer.service.common.IHouseService;
import com.yjq.programmer.service.common.IUserService;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-28 10:59
 */
/**
 * 后台房屋控制器
 */
@Controller
@RequestMapping("/admin/house")
public class HouseController {

    @Autowired
    private IHouseService houseService;

    @Autowired
    private IUserService userService;

    /**
     * 后台房屋列表页面
     * @param model
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/list')")
    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("userList", userService.selectByRoleId(UserRoleEnum.HOUSE_AGENT.getCode()));
        return "/admin/house/list";
    }

    /**
     * 后台房屋列表信息获取
     * @param page
     * @param rows
     * @param info
     * @param state
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/list')")
    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> list(Integer page, Integer rows, String info, Integer state, HttpServletRequest request){
        return houseService.getHouseList(page, rows, info, state, request);
    }

    /**
     * 后台添加房屋信息操作处理
     * @param house
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/add')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo<Boolean> add(House house, HttpServletRequest request){
        return houseService.addHouse(house, request);
    }

    /**
     * 后台修改房屋信息操作处理
     * @param house
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/edit')")
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo<Boolean> edit(House house, HttpServletRequest request){
        return houseService.editHouse(house, request);
    }

    /**
     * 后台删除房屋信息操作处理
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo<Boolean> delete(String ids){
        return houseService.deleteHouse(ids);
    }

    /**
     * 后台管理员修改房屋状态信息操作处理
     * @param state
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/edit_state_admin')")
    @PostMapping("/edit_state_admin")
    @ResponseBody
    public ResponseVo<Boolean> editStateByAdmin(Integer state, Long id){
        return houseService.editStateByAdmin(id, state);
    }

    /**
     * 后台中介修改房屋状态信息操作处理
     * @param state
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/house/edit_state_agent')")
    @PostMapping("/edit_state_agent")
    @ResponseBody
    public ResponseVo<Boolean> editStateByAgent(Integer state, Long id){
        return houseService.editStateByAgent(id, state);
    }

}
