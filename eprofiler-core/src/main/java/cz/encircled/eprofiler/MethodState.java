package cz.encircled.eprofiler;

/**
 * @author Kisel on 24.05.2016.
 */
public interface MethodState {

    void setStartTime(Long time);

    void end();

    void addParam(String value);

    boolean hasParent();

    MethodState addNested(MethodState state);

}
