package com.atomicrobot.carbon.ui.main

import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails
import com.atomicrobot.carbon.data.lumen.Device
import com.atomicrobot.carbon.data.lumen.LightType
import com.atomicrobot.carbon.data.lumen.Room
import com.atomicrobot.carbon.data.lumen.Scene
import com.atomicrobot.carbon.data.lumen.SceneDevice

val dummyCommits = listOf(
    Commit(commit = CommitDetails(message = "Sample github commit message #1", author = Author("Smitty Joe"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #2", author = Author("Joe Smitty"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #3", author = Author("Smith Joe"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #4", author = Author("Joe Smith"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #5", author = Author("Joe Smoe")))
)

val dummyMasterBedroom = Room("Master Bedroom")
val dummyLivingRoom = Room("Living Room")
val dummyGardenRoom = Room("Indoor Garden")
val dummyMasterBathroom = Room("Master Bathroom")
val dummyKitchenRoom = Room("Kitchen")
val dummyStudyRoom = Room("Study")

val masterBedroomDevices: List<Device> = listOf(
    Device("Floor Lamp", dummyMasterBedroom),
    Device("Ceiling Fan", dummyMasterBedroom),
    Device("Night Stand R", dummyMasterBedroom),
    Device("Night Stand L", dummyMasterBedroom),
    Device("Closet Light", dummyMasterBedroom),
)

val livingRoomDevices: List<Device> = listOf(
    Device("TV Lamp L", dummyLivingRoom),
    Device("TV Lamp R", dummyLivingRoom),
    Device("Ceiling Fan", dummyLivingRoom),
    Device("Coffee Table", dummyLivingRoom),
)

val masterBathroomDevices: List<Device> = listOf(
    Device("Mirror Light #1", dummyMasterBathroom),
    Device("Mirror Light #2", dummyMasterBathroom),
    Device("Mirror Light #3", dummyMasterBathroom),
)

val dummyDevices: List<Device> = masterBedroomDevices
    .plus(livingRoomDevices)
    .plus(masterBathroomDevices)

val gradenScene = Scene("Grow Lights", room = dummyGardenRoom, duration = "8 Hours", favorite = true)

val livingRoomScenes = listOf(
    Scene("Movie Night", room = dummyLivingRoom, duration = "3hrs"),
    Scene("Reading", room = dummyLivingRoom, duration = "45min left", active = true),
)

val studyScenes: List<Scene> = listOf(
    Scene(
        name = "Zoom Call",
        room = dummyStudyRoom,
        duration = "45min left",
        active = true,
        favorite = true
    ),
)

val kitchenScenes: List<Scene> = listOf(
    Scene("Cooking", room = dummyKitchenRoom, duration = "2hrs"),
    Scene("Entertaining", room = dummyKitchenRoom, duration = "4hrs"),
)

val dummyScenes: List<Scene> = listOf(gradenScene)
    .plus(livingRoomScenes)
    .plus(studyScenes)
    .plus(kitchenScenes)

val growSceneDevices: List<SceneDevice> = listOf(
    SceneDevice(
        gradenScene,
        Device("Pathos", dummyGardenRoom, type = LightType.COLOR), brightness = .75F
    ),
    SceneDevice(
        gradenScene,
        Device("Palms", dummyGardenRoom, type = LightType.COLOR), brightness = 0F
    ),
    SceneDevice(
        gradenScene,
        Device("Seeds", dummyGardenRoom, type = LightType.COLOR), brightness = 0.5F
    ),
)

val dummyRooms: List<Room> = listOf(
    dummyMasterBedroom.apply {
        devices.addAll(masterBedroomDevices)
    },
    dummyLivingRoom.apply {
        devices.addAll(livingRoomDevices)
        scenes.addAll(livingRoomScenes)
    },
    dummyMasterBathroom.apply {
        devices.addAll(masterBathroomDevices)
    },
    dummyKitchenRoom.apply {
        scenes.addAll(kitchenScenes)
    },
    dummyStudyRoom.apply {
        scenes.addAll(studyScenes)
    },
    dummyGardenRoom.apply {
        scenes.add(
            gradenScene.apply {
                devices.addAll(growSceneDevices)
            }
        )
    },
)
