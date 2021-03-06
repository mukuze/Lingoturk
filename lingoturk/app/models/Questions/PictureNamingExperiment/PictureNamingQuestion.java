package models.Questions.PictureNamingExperiment;

import com.fasterxml.jackson.databind.JsonNode;
import play.db.ebean.Model;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.sql.SQLException;

@Entity
@Table(name="PictureNamingExperiment_PictureNaming")
public class PictureNamingQuestion extends Model {

    /* BEGIN OF VARIABLES BLOCK */

    @Id
    public int id;

    @Basic
    @Column(name="fileName", columnDefinition = "TEXT")
    public String fileName;

    @Basic
    @Column(name="additionalExplanations", columnDefinition = "TEXT")
    public String text;

    /* END OF VARIABLES BLOCK */

    public PictureNamingQuestion(JsonNode node){
        this.fileName = node.get("fileName").asText();
        this.text = node.get("text").asText();
    }

    public JsonObject returnJSON() throws SQLException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("id",id);
        objectBuilder.add("fileName",fileName);
        objectBuilder.add("text",text);

        return objectBuilder.build();
    }
}
