package jp.kobespiral.odajin.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.kobespiral.odajin.todo.dto.LoginForm;
import jp.kobespiral.odajin.todo.dto.ToDoForm;
import jp.kobespiral.odajin.todo.entity.ToDo;
import jp.kobespiral.odajin.todo.service.MemberService;
import jp.kobespiral.odajin.todo.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    MemberService mService;

    @Autowired
    ToDoService tService;


    @GetMapping("/")
    //"/"にGetアクセスが有ったとき，このアノテーションが付与された関数が呼び出される
    public String showLandingPage(
    Model model,
    @ModelAttribute
    LoginForm form) {
        model.addAttribute("LoginForm", new LoginForm());
        return "index";
    }

    @PostMapping("/enter")
    //"/enter"にPostアクセスが有ったとき，このアノテーションが付与された関数が呼び出される
    public String confirmUserExistence(
    Model model,
    //index.htmlからPostされたデータを，formで受け取る
    RedirectAttributes attributes,
    @ModelAttribute
    LoginForm form
    // @PathVariable("mid")
    // String mid
    ) {
        String mid = form.getMid();
        //ユーザが存在するかチェック
        if(!mService.checkExistenceMenber(mid)){
            attributes.addFlashAttribute("isMidNotFound", true);
            return "redirect:/";
        }
        //存在しないなら，リダイレクト

        //存在するなら
        //midをもとにその人のToDoデータを取得
        //取得したToDoデータを用いて，list.htmlを描画

        return "redirect:/todo/"+mid;
    }
    @GetMapping("/todo/{mid}")
    public String showToDoListOfUser(
        Model model,
        @PathVariable
        String mid,
        @ModelAttribute
        ToDoForm form
    ){
        //midのToDo情報を取得
        //Modelに詰めて，todolistで描画
        List<ToDo> mid_ToDos = tService.getToDoList(mid);
        model.addAttribute("mid_ToDos", mid_ToDos);

        //ToDo入力用の空のフォームを用意
        model.addAttribute("ToDoForm", new ToDoForm());
        //todolist.htmlで使用するため，modelにmidを追加
        model.addAttribute("mid",mid);
        return "todolist";
    }
    @PostMapping("/todo/{mid}/register")
    String createToDo(
        @PathVariable String mid,
        @ModelAttribute(name = "ToDoForm") 
        ToDoForm form, 
        Model model
        ) {
        ToDo t = tService.createToDo(mid, form);//midなどの情報も渡したい
        model.addAttribute("ToDoForm", t);
        return "redirect:/todo/"+mid;
    }

}
