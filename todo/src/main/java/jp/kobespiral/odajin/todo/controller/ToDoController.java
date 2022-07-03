package jp.kobespiral.odajin.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.kobespiral.odajin.todo.dto.LoginForm;
import jp.kobespiral.odajin.todo.dto.ToDoForm;
import jp.kobespiral.odajin.todo.entity.Member;
import jp.kobespiral.odajin.todo.entity.ToDo;
import jp.kobespiral.odajin.todo.exception.ToDoAppException;
import jp.kobespiral.odajin.todo.service.MemberService;
import jp.kobespiral.odajin.todo.service.ToDoService;
import jp.kobespiral.odajin.todo.dto.UserDetailsImpl;

@Controller
public class ToDoController {
    @Autowired
    MemberService mService;

    @Autowired
    ToDoService tService;

    /**
     * トップページ
     */
    @GetMapping("/sign_in")
    String showIndex(@RequestParam Map<String, String> params, @ModelAttribute LoginForm form, Model model) {
        // パラメータ処理．ログアウト時は?logout, 認証失敗時は?errorが帰ってくる（WebSecurityConfig.java参照）
        if (params.containsKey("sign_out")) {
            model.addAttribute("message", "サインアウトしました");
        } else if (params.containsKey("error")) {
            model.addAttribute("message", "サインインに失敗しました");
        }
        // model.addAttribute("loginForm", loginForm);
        return "signin";
    }

    /**
     * ログイン処理．midの存在確認をして，ユーザページにリダイレクト
     */
    @GetMapping("/sign_in_success")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (m.getRole().equals("ADMIN")) {
            return "redirect:/admin/register";
        }
        return "redirect:/todo/" + m.getMid();
    }

    @GetMapping("/")
    // "/"にGetアクセスが有ったとき，このアノテーションが付与された関数が呼び出される
    public String showLandingPage(
            Model model,
            @ModelAttribute LoginForm form) {
        model.addAttribute("LoginForm", new LoginForm());
        return "index";
    }

    // ログイン処理
    @PostMapping("/enter")
    public String confirmUserExistence(
            Model model,
            // index.htmlからPostされたデータを，formで受け取る
            RedirectAttributes attributes,
            @ModelAttribute LoginForm form
    // @PathVariable("mid")
    // String mid
    ) {
        String mid = form.getMid();
        // ユーザが存在するかチェック
        if (!mService.checkExistenceMenber(mid)) {
            attributes.addFlashAttribute("isMidNotFound", true);
            return "redirect:/";
        }
        // 存在しないなら，リダイレクト

        // 存在するなら
        // midをもとにその人のToDoデータを取得
        // 取得したToDoデータを用いて，list.htmlを描画

        return "redirect:/todo/" + mid;
    }

    // ユーザのマイページ
    @GetMapping("/todo/{mid}")
    public String showToDoListOfUser(
            @PathVariable String mid,
            @ModelAttribute ToDoForm form,
            Model model) {

        checkIdentity(mid);

        // midのToDo情報を取得
        // Modelに詰めて，todolistで描画
        List<ToDo> ToDos = tService.getToDoList(mid);
        model.addAttribute("ToDos", ToDos);

        // midのDone情報を取得
        // Modelに詰めて，todolistで描画
        List<ToDo> Dones = tService.getDoneList(mid);
        model.addAttribute("Dones", Dones);

        // ToDo入力用の空のフォームを用意
        model.addAttribute("ToDoForm", form);
        // todolist.htmlで使用するため，modelにmidを追加
        model.addAttribute("mid", mid);
        return "todolist";
    }

    @PostMapping("/todo/{mid}/register")
    String createToDo(
            @PathVariable String mid,
            @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        checkIdentity(mid);
        if (bindingResult.hasErrors()) {
            // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
            return showToDoListOfUser(mid, form, model);
        }
        ToDo t = tService.createToDo(mid, form);// midなどの情報も渡したい
        model.addAttribute("ToDoForm", t);
        return "redirect:/todo/" + mid;
    }

    // 指定したユーザのToDoを完了済みに変更する
    @GetMapping("/todo/{mid}/{str_seq}")
    String doneToDo(
            @PathVariable String mid,
            @PathVariable String str_seq) {
        checkIdentity(mid);
        Long seq = Long.parseLong(str_seq);
        // 更新対象のToDoを取得
        tService.doneToDo(mid, seq);

        return "redirect:/todo/" + mid;
    }

    @GetMapping("/allmembers")
    public String showAllMembersList(
            Model model) {
        // 全員のToDo情報を取得
        // Modelに詰めて，allMembersList.htmlで描画
        List<ToDo> ToDos = tService.getToDoList();
        model.addAttribute("allToDos", ToDos);

        // 全員のDone情報を取得
        // Modelに詰めて，allMembersListで描画
        List<ToDo> Dones = tService.getDoneList();
        model.addAttribute("allDones", Dones);

        return "allMembersList";

    }

    private void checkIdentity(String mid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (!mid.equals(m.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_TODO_OPERATION,
                    m.getMid() + ": not authorized to access resources of " + mid);
        }
    }
}
