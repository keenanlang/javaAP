YAWT (Yet Another Windowing Toolkit) version 0.7.0

Change List
    0.7.0
        class Point changed to Vector
        Component.put changed to Component.position
        Container.componentat changed to Container.survey
        Container.focus() and Container.focus(Component) added to control focused components
        raise method added to raise your own events
        Container now uses the order of its components to track focus
        Container now draws components in reverse order of focus
        Container.survey uses focus priority for overlapping components
        Display.size works correctly as does resizing the frame
        Display's Frame and Canvas no longer painting over custom components in a new frame
        Display.render rewritten to use the idiomatic java 1.6 style buffered strategy rendering
        