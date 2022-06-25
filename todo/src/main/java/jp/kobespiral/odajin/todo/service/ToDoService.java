
package jp.kobespiral.odajin.todo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.kobespiral.odajin.todo.dto.ToDoForm;
import jp.kobespiral.odajin.todo.entity.ToDo;
import jp.kobespiral.odajin.todo.exception.ToDoAppException;
import jp.kobespiral.odajin.todo.repository.ToDoRepository;

@Service
public class ToDoService {
    @Autowired
    ToDoRepository tRepo;

    public ToDo createToDo(String mid,ToDoForm form){

    //title,作成者，済マーク，作成日時，完了日時を入れたToDoエンティティを作成
    ToDo t = new ToDo();
    t.setMid(mid);

    t.setDone(false);
    t.setCreatedAt(new Date());
    t.setTitle(form.getTitle());

    return tRepo.save(t);
    }
    public ToDo getToDo(Long seq) {
        ToDo t = tRepo.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_TODO_EXISTS, seq + ": No such todo exists"));
        return t;
    }

    //midのToDoリストを取得
    public List<ToDo> getToDoList(String mid) {
        return tRepo.findByMidAndDone(mid, false);
    }

    //midのDoneリストを取得
    public List<ToDo> getDoneList(String mid) {
        return tRepo.findByMidAndDone(mid, true);
    }

    //midのもつID:seqのToDoについて，完了済とする
    public ToDo doneToDo(String mid,Long seq){
        ToDo t = tRepo.findByMidAndSeq(mid,seq);
        t.setDone(true);
        return tRepo.save(t);
    }

    //全員のToDoリストを取得
    public List<ToDo> getToDoList(){
        return tRepo.findByDone(false);
    }

    //全員のDoneリストを取得
    public List<ToDo> getDoneList(){
        return tRepo.findByDone(true);
    }
}
