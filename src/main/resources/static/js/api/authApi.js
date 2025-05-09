import { apiRequest } from '../utils/apiRequest.js';

export function registerUser(name, email, password) {
  return apiRequest('users/register', 'POST', { name, email, password });
}

export function loginUser(email, password) {
  return apiRequest('users/login', 'POST', { email, password });
}
