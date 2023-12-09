#!/bin/bash
export LIB_PATH=javafx-sdk-17.0.9/lib/
exec java --module-path $LIB_PATH --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media -jar PlantVsZombies.jar
