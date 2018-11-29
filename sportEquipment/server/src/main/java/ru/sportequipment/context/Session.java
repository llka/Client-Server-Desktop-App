package ru.sportequipment.context;


import ru.sportequipment.entity.Visitor;

public class Session {
    private Visitor visitor;

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public String toString() {
        return "Session{" +
                "visitor=" + visitor +
                '}';
    }
}
