import { apiRequest } from '../utils/apiRequest.js';

export function getUserNotes(userId) {
  return apiRequest(`notes/${userId}/notes`);
}

export function getNoteVersions(noteId) {
  return apiRequest(`notes/${noteId}/versions`);
}

export function restoreNoteVersion(noteId, versionId) {
  return apiRequest(`notes/${noteId}/restore/${versionId}`, 'PUT');
}

export function shareNote(noteId, email, permission) {
  return apiRequest(`notes/${noteId}/share`, 'POST', {
    sharedWithUserEmail: email,
    permission: permission
  });
}

export function addNote(note) {
  return apiRequest('notes/add', 'POST', note);
}