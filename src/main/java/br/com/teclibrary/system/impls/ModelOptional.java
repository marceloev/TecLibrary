package br.com.teclibrary.system.impls;

public class ModelOptional<OptionalObject> {

    public ModelOptional() {

    }

    public ModelOptional(OptionalObject object) {
        this.object = object;
    }

    private OptionalObject object;

    public OptionalObject get() {
        return object;
    }

    public void set(OptionalObject object) {
        this.object = object;
    }

    public Boolean contains() {
        return object != null;
    }
}
