package jp.kobespiral.odajin.todo.entity;//適宜変えること

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long seq;
    String title;
    String mid;
    boolean done;
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    Date doneAt;

}