{{- define "platform-service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end }}

{{- define "platform-service.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- include "platform-service.name" . -}}
{{- end -}}
{{- end }}

{{- define "platform-service.labels" -}}
app.kubernetes.io/name: {{ include "platform-service.name" . }}
helm.sh/chart: {{ printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/part-of: gitops-platform
{{- if .Values.environment }}
app.kubernetes.io/environment: {{ .Values.environment }}
{{- end }}
{{- end }}

{{- define "platform-service.selectorLabels" -}}
app.kubernetes.io/name: {{ include "platform-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
