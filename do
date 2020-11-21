#!/bin/bash

((nextParam = 1))
for ((cmd = 1; cmd <= $#; cmd++)); do

    ((nextParam++))

    case "${!cmd}" in

    "help")
        echo "w \$V:    set gradle wrapper"
        echo "fixgit:  fix permission flag on git index for required files"
        echo "run :    run plugin in IntelliJ Ultimate 2020.2"
        echo "runeap:  run plugin in latest IntelliJ Ultimate EAP Snapshot"
        echo "release: package plugin"
        echo "test:    run unit tests"
        echo "cv:      check dependencies and Gradle updates"
        echo "svgo:    optimize SVG icons with SGVO. SVGO must be present, type 'npm install -g svgo' if needed)"
        ;;

    "w")
        gradle wrapper --gradle-version=%2 --no-daemon
        ;;

    "fixgit")
        git update-index --chmod=+x "gradlew"
        echo "'gradlew' has now executable flag on git gradlew"
        git update-index --chmod=+x "do"
        echo "'do' has now executable flag on git index"
        ;;

    "run")
        ./gradlew buildPlugin runIde --warning-mode all
        ;;

    "runeap")
        ./gradlew buildPlugin runIde --warning-mode all -PpluginIdeaVersion=IU-LATEST-EAP-SNAPSHOT -PpluginDownloadIdeaSources=false
        ;;

    "release")
        ./gradlew clean buildPlugin test verifyPlugin --warning-mode all
        ;;

    "test")
        ./gradlew cleanTest test verifyPlugin --warning-mode all
        ;;

    "cv")
        ./gradlew dependencyUpdates --warning-mode all
        ;;

    "svgo")
        svgo --folder=src/main/resources/icons/ --multipass --config=svgo.yml
        ;;

    esac

done
