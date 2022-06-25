
package jp.kobespiral.odajin.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jp.kobespiral.odajin.todo.dto.ToDoForm;
import jp.kobespiral.odajin.todo.entity.ToDo;
import jp.kobespiral.odajin.todo.exception.ToDoAppException;
import jp.kobespiral.odajin.todo.repository.ToDoRepository;

public class ToDoService {
    @Autowired
    ToDoRepository tRepo;

    // public ToDo createToDo(String mid,ToDoForm form){

    // ToDo t = new ToDo()
    // return ;
    // }
    public ToDo getToDo(Long seq) {
        ToDo t = tRepo.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_TODO_EXISTS, seq + ": No such todo exists"));
        return t;
    }

    public List<ToDo> getToDoList(String mid) {
        return tRepo.findByMidAndDone(mid, false);
    }

    public List<ToDo> getDoneList(String mid) {
        return tRepo.findByMidAndDone(mid, true);
    }
}
