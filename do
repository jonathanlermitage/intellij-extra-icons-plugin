#!/bin/bash

((nextParam = 1))
for ((cmd = 1; cmd <= $#; cmd++)); do

    ((nextParam++))

    case "${!cmd}" in

    "help")
        echo "w \$V:    set or upgrade gradle wrapper to version \$V"
        echo "fixgit:  fix permission flag on git index for required files"
        echo "run :    run plugin in IntelliJ Ultimate"
        echo "runeap:  run plugin in latest IntelliJ Ultimate EAP Snapshot"
        echo "runold:  run plugin in oldest supported IntelliJ Ultimate version"
        echo "release: package plugin"
        echo "test:    run unit tests"
        echo "cv:      check dependencies and Gradle updates"
        echo "oga:     check for deprecated groupId and artifactId couples"
        echo "svgo:    optimize SVG icons with SGVO. SVGO must be present, type 'npm install -g svgo' if needed"
        echo "lock:    write gradle dependency versions lock file"
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
        ./gradlew buildPlugin runIde --warning-mode all -PpluginIdeaVersion=IC-LATEST-EAP-SNAPSHOT -PpluginDownloadIdeaSources=false
        ;;

    "runold")
        ./gradlew buildPlugin runIde --warning-mode all -PpluginIdeaVersion=IC-2021.1.3 -PpluginDownloadIdeaSources=false
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

    "oga")
        ./gradlew gradlew biz-lermitage-oga-gradle-check
        ;;

    "svgo")
        svgo --folder=src/main/resources/extra-icons/ --recursive
        ;;

    "lock")
        ./gradlew dependencies --write-locks
        ;;

    esac

done
