package models.Questions.ElicitingParaphrasesExperiment;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ElicitingParaphrasesResults")
public class ElicitingParaphrasesResults extends Model{

    @Id
    int id;

    @Basic
    String assignmentId;

    @Basic
    String hitId;

    @Basic
    String workerId;

    @Basic
    String origin;

    @Basic
    Date timestamp;

    @Basic
    int partId;

    @Basic
    int questionId;

    @Basic
    @Column(columnDefinition = "TEXT")
    String answer;
}