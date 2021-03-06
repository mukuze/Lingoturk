package models.Questions.LinkingV1Experiment;


import play.db.ebean.Model;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name="LinkingItem")
public class Item extends Model {

    /* BEGIN OF VARIABLES BLOCK */

    /* END OF VARIABLES BLOCK */

    @Id
    public int id;

    @Basic
    public String h;

    @Basic
    public String head;

    @Basic
    public String original;

    @Basic
    public int slot;

    @Basic
    public String text;

    @Column(name="script_questionId")
    @ManyToOne
    public Script script;

    @Column(name = "c", columnDefinition = "varchar(255) default ''")
    public String c;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Pair> pairs = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL)
    public List<PTCP> ptcps = new LinkedList<>();

    private static Finder<Integer,Item> finder = new Finder<>(Integer.class,Item.class);

    public Item(String h, String pair, String text, String slot, String original, String head, String c) {
        this.h = h;

        if(!pair.equals("")){
            String[] pairs = pair.split(";");
            for (String p : pairs){
                if(!p.startsWith("(")){
                    if(p.equals("none")){
                        this.pairs.add(new Pair(true));
                    }else{
                        this.pairs.add(new Pair(p.split(",")[0],p.split(",")[1]));
                    }
                }else{
                    Pattern pattern = Pattern.compile("[0-9]+");
                    Matcher matcher = pattern.matcher(p);

                    String targetSource = null;
                    if(matcher.find()){
                        targetSource = matcher.group();
                    }

                    String targetId = null;
                    if(matcher.find()){
                        targetId = matcher.group();
                    }

                    String targetSlot = null;
                    if(matcher.find()){
                        targetSlot = matcher.group();
                    }
                    this.pairs.add(new Pair(targetSource,targetId,targetSlot));
                }
            }
        }

        this.text = text;
        this.slot = Integer.parseInt(slot);
        this.original = original;
        this.head = head;
        this.c = c;
    }

    @Override
    public String toString(){
        return "item : "
                + "\n\th : " + h
                + "\n\tc : " + c
                + "\n\thead : " + head
                + "\n\toriginal : " + original
                + "\n\tslot : " + slot
                + "\n\ttext : " + text
                + "\n\tpairs : " + pairs
                + "\n\tptcps: " + ptcps;
    }

    public void addPTCP(PTCP ptcp){
        ptcps.add(ptcp);
    }

    public JsonObject returnJSON() {
        JsonArrayBuilder pairBuilder = Json.createArrayBuilder();
        for(Pair p : pairs){
            pairBuilder.add(p.toJson());
        }

        JsonArrayBuilder ptcpBuilder = Json.createArrayBuilder();
        for(PTCP ptcp: ptcps){
            ptcpBuilder.add(ptcp.toJson());
        }

        return Json.createObjectBuilder().add("isActive",h.isEmpty() ? false : true)
                .add("h",h)
                .add("id",id)
                .add("head",head)
                .add("original",original)
                .add("c",c)
                .add("slot",slot)
                .add("text",text)
                .add("pairs",pairBuilder.build())
                .add("ptcps",ptcpBuilder.build())
                .build();
    }

    public List<Pair> getPairs(){
        return pairs;
    }

    public String getH(){
        return h;
    }

    public int getSlot(){
        return slot;
    }

    public static class ItemSlotComparator implements Comparator<Item>{
        @Override
        public int compare(Item o1, Item o2) {
            return Integer.compare(o1.slot,o2.slot);
        }
    }

    static Item byId(int id){
        return finder.byId(id);
    }

}
