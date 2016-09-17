package com.green.card.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Main {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.membership.card.dao");// 객체가만들어질 위치

        Entity person = schema.addEntity("Card"); // Person 테이블추가
        person.addIdProperty();
        person.addStringProperty("cardName");
        person.addStringProperty("cardNumber");
        person.addStringProperty("cardNumberType");
        person.addStringProperty("cardBackground");




        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}

