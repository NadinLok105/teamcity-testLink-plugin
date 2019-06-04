package ru.nadin.tests.plugin.testLink.runner;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TestLinkRunTypePropertiesProcessor implements PropertiesProcessor {

    public Collection<InvalidProperty> process(Map<String, String> properties) {
        List<InvalidProperty> result = new Vector();
//
//        final String test = properties.get(Constants.SETTINGS_ALL_TESTS_ID);
//
//        if (PropertiesUtil.isEmptyOrNull(test)) {
//            result.add(new InvalidProperty(Constants.SETTINGS_ALL_TESTS_ID, "A test must be selected."));
//        }

        return result;
    }

}
