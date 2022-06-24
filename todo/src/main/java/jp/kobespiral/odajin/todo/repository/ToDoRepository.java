package jp.kobespiral.odajin.todo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jp.kobespiral.odajin.todo.entity.ToDo;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    List<ToDo> findAll();

    List<ToDo> findByMidAndDone(String mid, Boolean done);

    List<ToDo> findByDone(Boolean done);
}
