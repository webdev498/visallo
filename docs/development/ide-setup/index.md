# Visallo IDE Setup

Follow these instructions to setup [IntelliJ IDEA](https://www.jetbrains.com/idea/)
for Visallo development.

## Prerequisites

1. Install the required [devleopment dependencies](../dependencies.md).
1. Clone the open source repository from GitHub:
```bash
        git clone https://github.com/v5analytics/visallo.git
```

## Import Project

From the IntelliJ File menu or launch screen, select **Import Project**.
![IntelliJ Project Import Step 1](img/intellij-import-1.png)

Choose the `visallo` directory cloned from GitHub and click **OK**.
![IntelliJ Project Import Step 2](img/intellij-import-2.png)

Select **Import project from external model**, be sure to choose **Maven**, and click **Next**.
![IntelliJ Project Import Step 3](img/intellij-import-3.png)

There are a lot of settings to choose from here. Just make your selections match the screenshot and click **Next**.
![IntelliJ Project Import Step 4](img/intellij-import-4.png)

The **build-docline-none** profile should already be selected. Leave it as is and click **Next**.
![IntelliJ Project Import Step 5](img/intellij-import-5.png)

There should only be one Maven project to import and it should already be selected. If not, choose **visallo** and click **Next**.
![IntelliJ Project Import Step 6](img/intellij-import-6.png)

This screenshot shows the selection of JDK version **1.8**. You may need to click
the plus button near the top left to navigate the filesystem to your installation
of JDK 1.8 if you've never done it before. Click **Next** once your screen
looks similar.
![IntelliJ Project Import Step 7](img/intellij-import-7.png)

The last step is to name the IntelliJ project. The default should be fine, but
you can change it to something else if you really want. Click **Finish**.
![IntelliJ Project Import Step 8](img/intellij-import-8.png)

## Restore Run Configurations

Visallo ships with a collection of useful run configurations for IntelliJ.
Unfortunately, importing the project into IntelliJ deletes them from their
proper location. To get them back, run the following command in the
`visallo` directory:

```bash
        git checkout .idea/
```

The above command will restore several files in the `.idea/runConfigurations`
directory. These files are the settings for each of the IntelliJ run configurations
that ship with Visallo. They should automatically show up in the
`Run -> Edit Configurations` dialog in IntelliJ.
