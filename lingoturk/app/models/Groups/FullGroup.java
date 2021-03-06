package models.Groups;

import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.Application;
import models.LingoExpModel;
import models.Worker;
import play.data.DynamicForm;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.json.JsonObject;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import static controllers.RenderController.getRenderMethod;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.ok;


@Entity
@Inheritance
@DiscriminatorValue("FullGroup")
public class FullGroup extends AbstractGroup {

    /* BEGIN OF VARIABLES BLOCK */

    /* END OF VARIABLES BLOCK */

    public FullGroup(){}

    public String publishOnAMT(RequesterService service, int publishedId, String hitTypeId, Long lifetime, Integer maxAssignments) throws SQLException {

        String question = "<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2006-07-14/ExternalQuestion.xsd\">"
                + "<ExternalURL> " + Application.getStaticIp() + "/render?id=" + getId() + "&amp;Type=part</ExternalURL>"
                + "<FrameHeight>" + 800 + "</FrameHeight>" + "</ExternalQuestion>";
        HIT hit = service.createHIT(hitTypeId, null, null, null, question, null, null, null, lifetime, maxAssignments, null, null, null, null, null, null);
        String url = service.getWebsiteURL() + "/mturk/preview?groupId=" + hit.getHITTypeId();

        insert(hit.getHITId(), publishedId);

        availability = maxAssignments;
        update();

        return url;
    }

    @Override
    public List<ProlificPublish> prepareProlificPublish() {
        return null;
    }

    @Override
    public void publishOnProlific(int maxAssignments) {

    }

    @Override
    public Result renderAMT(Worker worker, String assignmentId, String hitId, String turkSubmitTo, LingoExpModel exp, DynamicForm df){
        try {
            Method m = getRenderMethod(exp.getExperimentType());
            Html webpage = (Html) m.invoke(null, null, this, worker, assignmentId, hitId, turkSubmitTo, exp, df, "MTURK");
            return ok(webpage);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return internalServerError("Could not load experiment of type: " + exp.getExperimentType());
        }
    }

    @Override
    public void setJSONData(LingoExpModel experiment, JsonNode partNode) throws SQLException {
        super.setJSONData(experiment,partNode);
    }

    @Override
    public JsonObject returnJSON() throws SQLException {
        return super.returnJSON();
    }

}
