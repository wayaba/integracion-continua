#  Copyright IBM Corporation 2018.
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v20.html

FROM ppedraza/iib

ENV BAR1=abc.bar


# Copy in the bar file to a temporary directory
RUN mkdir /tmp/BARs
RUN mkdir /tmp/scripts
RUN mkdir /tmp/artifacts

COPY $BAR1 /tmp/BARs

RUN sudo chown -R iibuser /tmp