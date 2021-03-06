package models.Questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.LingoExpModel;
import controllers.DatabaseController;
import models.Worker;
import play.data.DynamicForm;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.mvc.Result;

import javax.json.JsonObject;
import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Inheritance
@DiscriminatorColumn(length = 100)
@Table(name = "Questions")
@MappedSuperclass
public abstract class Question extends Model {

    @Id
    @Column(name = "QuestionID")
    protected int id;

    @JsonIgnore
    @Basic
    @Constraints.Required
    @Column(name = "LingoExpModelId")
    protected int experimentID;

    @JsonIgnore
    @Column(name = "Availability", columnDefinition = "integer default 1")
    protected int availability;

    @JsonIgnore
    @Column(name = "disabled", columnDefinition = "boolean default false")
    public boolean disabled;

    @Basic
    @Column(name = "subList", columnDefinition = "varchar(255) default ''")
    public String subList;

    @JsonIgnore
    private static Finder<Integer, Question> finder = new Finder<>(Integer.class, Question.class);

    public static Question byId(int id) {
        return finder.byId(id);
    }

    public synchronized Question getIfAvailable() throws SQLException {
        if (disabled){
            return null;
        }

        PreparedStatement statement = DatabaseController.getConnection().prepareStatement("SELECT * FROM Questions WHERE QuestionID=" + this.getId());
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            this.availability = rs.getInt("Availability");
        }

        if (availability > 0) {
            this.availability--;
            statement = DatabaseController.getConnection().prepareStatement("UPDATE Questions SET Availability = ? WHERE QuestionID = ?");
            statement.setInt(1, this.availability);
            statement.setInt(2, this.getId());
            statement.execute();
            return this;
        }

        return null;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public abstract void writeResults(JsonNode resultNode) throws SQLException;

    public int getId() {
        return id;
    }

    public abstract JsonObject returnJSON() throws SQLException;

    public abstract Result renderAMT(Worker worker, String assignmentId, String hitId, String turkSubmitTo, LingoExpModel exp, DynamicForm df);
}
