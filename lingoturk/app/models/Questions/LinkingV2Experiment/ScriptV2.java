package models.Questions.LinkingV2Experiment;

import com.fasterxml.jackson.databind.JsonNode;
import models.LingoExpModel;
import models.Questions.LinkingV1Experiment.Script;
import controllers.DatabaseController;
import models.Worker;
import play.data.DynamicForm;
import play.mvc.Result;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static play.mvc.Results.ok;

@Entity
@Inheritance
@DiscriminatorValue("LinkingV2Experiment.ScriptV2")
public class ScriptV2 extends Script {

    /* BEGIN OF VARIABLES BLOCK */

    /* END OF VARIABLES BLOCK */

    @Override
    public void writeResults(JsonNode resultNode) throws SQLException {
        String assignmentId = resultNode.get("assignmentId").asText();
        String hitId = resultNode.get("hitId").asText();
        String workerId = resultNode.get("workerId").asText();
        int lhs_script = resultNode.get("script_lhsId").asInt();
        int rhs_script = resultNode.get("script_rhsId").asInt();
        int lhs_slot = resultNode.get("lhs_slot").asInt();
        int rhs_slot = resultNode.get("rhs_slot").asInt();
        String result = resultNode.get("result").asText();

        PreparedStatement statement = DatabaseController.getConnection().prepareStatement(
                "INSERT INTO LinkingV2Results(id,WorkerId,AssignmentId,HitId,lhs_script,rhs_script,lhs_slot,rhs_slot,result) VALUES(nextval('LinkingV2Results_seq'),?,?,?,?,?,?,?,?)"
        );

        statement.setString(1, workerId);
        statement.setString(2, assignmentId);
        statement.setString(3, hitId);
        statement.setInt(4, lhs_script);
        statement.setInt(5, rhs_script);
        statement.setInt(6, lhs_slot);
        statement.setInt(7, rhs_slot);
        statement.setString(8, result);
        statement.execute();
        statement.close();
    }

    public ScriptV2(String scriptId, String source, String side, LingoExpModel exp) {
        super(scriptId, source, side, exp);
    }

    @Override
    public Result renderAMT(Worker worker, String assignmentId, String hitId, String turkSubmitTo, LingoExpModel exp, DynamicForm df) {
        return ok(views.html.ExperimentRendering.LinkingV2Experiment.LinkingV2Experiment_render.render(this, null, worker, assignmentId, hitId, turkSubmitTo, exp, df, "MTURK"));
    }
}
