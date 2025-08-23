package com.hospital.model;

/**
 * A simple data holder class to represent a Patient in a JComboBox.
 * This makes it easy to store both the ID and the name in the combo box item.
 */
public class PatientItem {
    public int id;
    public String name;

    public PatientItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
