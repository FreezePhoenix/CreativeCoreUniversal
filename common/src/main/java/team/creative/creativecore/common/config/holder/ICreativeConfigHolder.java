package team.creative.creativecore.common.config.holder;

import java.util.Collection;

import com.google.gson.JsonObject;

import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.sync.ConfigSynchronization;

public interface ICreativeConfigHolder {
    
    ICreativeConfigHolder parent();
    
    default String getName() {
        return path()[path().length - 1];
    }
    
    String[] path();
    
    Collection<? extends ConfigKey> fields();
    
    Collection<String> names();
    
    Object get(String key);
    
    ConfigKey getField(String key);
    
    boolean isDefault(Side side);
    
    void restoreDefault(Side side, boolean ignoreRestart);
    
    void load(boolean loadDefault, boolean ignoreRestart, JsonObject json, Side side);
    
    JsonObject save(boolean saveDefault, boolean ignoreRestart, Side side);
    
    boolean isEmpty(Side side);
    
    boolean isEmptyWithoutForce(Side side);
    
    ConfigSynchronization synchronization();
    
}
