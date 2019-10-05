package pub.tbc.dev.util.base.proxy;

/**
 *
 * @author tbc on 2017/6/11 09:39:35.
 */
public abstract class BeforeProxyOperation extends SurroundProxyOperation {
    @Override
    abstract public void before();

    @Override
    public void after() {

    }
}
