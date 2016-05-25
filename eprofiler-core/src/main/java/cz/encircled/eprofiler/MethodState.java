package cz.encircled.eprofiler;

/**
 * @author Kisel on 24.05.2016.
 */
public interface MethodState {

    long getId();

    void setId(long id);

    void setStartTime(Long time);

    void end();

    void addParam(String value);

    boolean hasParent();

    MethodState addNested(MethodState state);

}
