package com.github.thecrazyphoenix.societies;

import com.github.thecrazyphoenix.societies.api.SocietiesService;
import com.github.thecrazyphoenix.societies.api.Society;
import org.spongepowered.api.plugin.Plugin;

import java.util.Set;

@Plugin(id = "societies")
public class Societies {
    // TODO Configuration loading
    private Set<Society> societies;
    private Set<Society> allSocieties;

    private class SocietiesServiceImpl implements SocietiesService {
        @Override
        public Set<Society> getSocieties() {
            return societies;
        }

        @Override
        public Set<Society> getAllSocieties() {
            return societies;
        }
    }
}
