package net.codealizer.thegradebook.apis.ic.calendar;

import net.codealizer.thegradebook.apis.ic.schedule.TermSchedule;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;


public class ScheduleStructure implements Serializable {
    public String id;
    public String name;
    public ArrayList<TermSchedule> termSchedules;

    public ScheduleStructure(Element sceduleElement) {
        id = sceduleElement.getAttribute("structureID");
        name = sceduleElement.getAttribute("name");

        termSchedules = new ArrayList<>();

        NodeList list = sceduleElement.getElementsByTagName("TermSchedule");

        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            termSchedules.add(new TermSchedule(element));
        }


    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<TermSchedule> getTermSchedules() {
        return termSchedules;
    }
}
